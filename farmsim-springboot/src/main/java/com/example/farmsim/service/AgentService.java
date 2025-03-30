package com.example.farmsim.service;

import com.example.farmsim.model.entity.*;
import com.example.farmsim.repository.*;
import com.example.farmsim.websocket.Message;
import com.example.farmsim.websocket.WebSocketHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class AgentService {

    @Autowired
    private AgentRepo agentRepo;

    @Autowired
    private GLMService glmService;

    @Autowired
    private SimulationRepo simulationRepo;

    @Autowired
    private AgentDialogueService agentDialogueService;

    @Autowired
    private AgentDialogueRepo agentDialogueRepo;

    @Autowired
    private EnvironmentRepo environmentRepo;

    @Autowired
    private AgentCommandRepo agentCommandRepo;

    @Autowired
    private CropRepo cropRepo;

    @Autowired
    private UserGoalRepo userGoalRepo;

    @Autowired
    private WebSocketHandler webSocketHandler;

    @Autowired
    private OptimizationActionRepo optimizationActionRepo;



    /**
     * 启动对话，仅生成自然语言对话，不包含 JSON 指令
     */
    public String startDialogue(String simulationId, String prompt) {
        try {
            // 1. 获取当前模拟的环境数据（最新一条）
            Environment latestEnv = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);
            if (latestEnv == null) {
                throw new RuntimeException("当前模拟没有环境数据");
            }

            Simulation simulation = simulationRepo.findById(simulationId)
                    .orElseThrow(() -> new RuntimeException("模拟不存在: " + simulationId));

            // 保存当前 prompt
            simulation.setCurrentPrompt(prompt);
            simulationRepo.save(simulation);

            // 2. 获取当前模拟的作物列表
            List<Crop> crops = cropRepo.findBySimulationId(simulationId);
            if (crops.isEmpty()) {
                throw new RuntimeException("当前模拟没有作物数据");
            }

            // 3. 构建包含环境和作物信息的提示
            String envInfo = String.format(
                    "当前环境：温度 %.1f℃, 土壤肥力 %.0f%%, 降水量 %.1fmm, 地形: %s, 气候: %s, 农业技术: %s",
                    latestEnv.getTemperature(),
                    latestEnv.getSoilFertility() * 100,
                    latestEnv.getPrecipitation(),
                    latestEnv.getTerrain(),
                    latestEnv.getClimate(),
                    latestEnv.getAgriculturalTechnology()
            );

            String cropInfo = crops.stream()
                    .map(c -> String.format("%s（生长率 %.2f）", c.getCropName(), c.getGrowthRate()))
                    .collect(Collectors.joining(", "));

            // 4. 获取当前模拟中的所有 Agent
            List<Agent> agents = agentRepo.findBySimulationId(simulationId);
            if (agents.isEmpty()) {
                throw new RuntimeException("当前模拟中没有 Agent");
            }

            // 5. 初始化对话上下文
            List<Map<String, String>> dialogueHistory = new ArrayList<>();
            dialogueHistory.add(Map.of("role", "system", "content", "你是一个农业专家，请根据以下环境和作物数据提供建议。"));
            dialogueHistory.add(Map.of("role", "user", "content", envInfo + "\n" + cropInfo));
            dialogueHistory.add(Map.of("role", "user", "content", prompt));

            // 6. 记录所有 Agent 的回复
            StringBuilder dialogueResult = new StringBuilder();
            for (Agent agent : agents) {
                // 6.1 构建角色提示（不包含 JSON 指令）
                String rolePrompt = buildRolePrompt(agent.getRoleType(), dialogueHistory);

                // 6.2 调用大模型生成回复
                List<Map<String, String>> messages = new ArrayList<>(dialogueHistory);
                messages.add(Map.of("role", "user", "content", rolePrompt));
                String agentResponse = glmService.getResponseFromGLM(messages);

                // 6.3 保存对话记录
                saveDialogue(simulationId, agent.getAgentId(), agent.getRoleType(), agentResponse);
                // 6.4 更新对话历史
                dialogueHistory.add(Map.of("role", "assistant", "content", agentResponse));
                dialogueResult.append(String.format("%s（%s）说：%s\n",
                        agent.getAgentName(), agent.getRoleType(), agentResponse));
            }

            // 7. 返回完整对话结果
            return dialogueResult.toString();
        } catch (Exception e) {
            throw new RuntimeException("启动对话失败: " + e.getMessage());
        }
    }

    public String handleUserPrompt(String simulationId, String prompt) {
        try {
            // 1. 获取当前模拟的环境数据
            Environment latestEnv = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);
            if (latestEnv == null) {
                throw new RuntimeException("当前模拟没有环境数据");
            }

            // 2. 获取当前模拟的作物数据
            List<Crop> crops = cropRepo.findBySimulationId(simulationId);
            if (crops.isEmpty()) {
                throw new RuntimeException("当前模拟没有作物数据");
            }

            // 3. 构建对话上下文
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", "你是一个农业专家，请根据以下环境和作物数据提供建议。"));
            messages.add(Map.of("role", "user", "content", buildEnvironmentSummary(latestEnv, crops)));
            messages.add(Map.of("role", "user", "content", prompt));

            // 4. 调用大模型生成回复
            return glmService.getResponseFromGLM(messages);
        } catch (Exception e) {
            throw new RuntimeException("处理用户问题失败: " + e.getMessage());
        }
    }


    private String buildEnvironmentSummary(Environment env, List<Crop> crops) {
        return String.format(
                "温度 %.1f℃, 土壤肥力 %.0f%%, 降水量 %.1fmm, 作物: %s",
                env.getTemperature(),
                env.getSoilFertility() * 100,
                env.getPrecipitation(),
                crops.stream().map(Crop::getCropName).collect(Collectors.joining(", "))
        );
    }

    private double calculateCropYield(Crop crop, Environment env) {
        return crop.getGrowthRate() *
                (1 + env.getTemperature() * crop.getTempWeight()) *
                (1 + env.getSoilFertility() * crop.getSoilWeight()) *
                (1 + env.getPrecipitation() * crop.getWaterWeight());
    }

    public String handleUserGoal(String simulationId, Map<String, Object> goal) {
        try {
            // 1. 保存用户目标
            UserGoal userGoal = new UserGoal();
            userGoal.setCropType(goal.get("cropType").toString());
            userGoal.setTargetYield(Double.parseDouble(goal.get("targetYield").toString()));
            userGoal.setTimestamp(LocalDateTime.now());

            Simulation simulation = simulationRepo.findById(simulationId)
                    .orElseThrow(() -> new RuntimeException("模拟不存在: " + simulationId));
            userGoal.setSimulation(simulation);

            userGoalRepo.save(userGoal);

            // 2. 生成基于目标的决策
            return generateGoalDrivenDecision(simulationId, goal);
        } catch (Exception e) {
            throw new RuntimeException("处理用户目标失败: " + e.getMessage());
        }
    }

    private String generateGoalDrivenDecision(String simulationId, Map<String, Object> goal) {
        Environment env = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);
        List<Crop> crops = cropRepo.findBySimulationId(simulationId);

        String cropType = goal.get("cropType").toString();
        double targetYield = Double.parseDouble(goal.get("targetYield").toString());

        // 根据目标生成建议（示例逻辑）
        StringBuilder advice = new StringBuilder();
        advice.append("目标设定：").append(cropType).append("产量达到").append(targetYield).append("吨\n");

        // 分析当前产量差距
        Crop targetCrop = crops.stream()
                .filter(c -> c.getCropName().equals(cropType))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("未找到作物：" + cropType));

        double currentYield = calculateCropYield(targetCrop, env);
        if (currentYield < targetYield) {
            double gap = targetYield - currentYield;
            advice.append("当前产量为").append(currentYield).append("吨，需提升").append(gap).append("吨\n");
            advice.append("建议措施：\n");
            advice.append("- 增加灌溉量以提高土壤湿度\n");
            advice.append("- 施用高效肥料提升土壤肥力\n");
        }

        return advice.toString();
    }

    public String generateAutomaticDecision(String simulationId) {
        Environment env = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);
        List<Crop> crops = cropRepo.findBySimulationId(simulationId);

        StringBuilder advice = new StringBuilder("环境检测报告：\n");
        advice.append("- 当前温度：").append(env.getTemperature()).append("℃\n");
        advice.append("- 土壤肥力：").append(env.getSoilFertility() * 100).append("%\n");
        advice.append("- 降水量：").append(env.getPrecipitation()).append("mm\n");

        // 自动分析问题并生成建议
        if (env.getPrecipitation() < 50) {
            advice.append("检测到降水量不足，建议启动灌溉系统。\n");
        }
        if (env.getSoilFertility() < 0.6) {
            advice.append("土壤肥力较低，建议施用有机肥料。\n");
        }

        // 触发自动执行优化指令
        executeAutomaticCommands(simulationId, advice.toString());

        // 构建 WebSocket 消息
        Message message = new Message();
        message.setType("agent-automatic-decision");
        message.setContent("Agent 自动决策完成");
        message.setSimulationId(simulationId);
        message.setData(Map.of(
                "advice", advice.toString()
        ));

        // 通过 WebSocket 发送消息
        webSocketHandler.broadcastMessage(message);

        return advice.toString();
    }

    private void executeAutomaticCommands(String simulationId, String advice) {
        // 解析建议生成指令（示例：自动灌溉）
        if (advice.contains("启动灌溉系统")) {
            Map<String, Object> commandParams = new HashMap<>();
            commandParams.put("action", "irrigate");
            commandParams.put("waterAmount", 200); // 灌溉200mm

            // 保存指令到AgentCommand
            AgentCommand command = new AgentCommand();
            command.setAction("irrigate");
            command.setParameters(commandParams.toString());
            command.setTimestamp(LocalDateTime.now());

            Simulation simulation = simulationRepo.findById(simulationId)
                    .orElseThrow(() -> new RuntimeException("模拟不存在: " + simulationId));
            command.setSimulation(simulation);

            agentCommandRepo.save(command);

            // 更新环境数据
            Environment env = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);
            env.setPrecipitation(env.getPrecipitation() + 200);
            environmentRepo.save(env);
        }
    }

    public String generateJsonCommand(String simulationId) {
        try {
            // 1. 查找模拟
            Simulation simulation = simulationRepo.findById(simulationId)
                    .orElseThrow(() -> new RuntimeException("模拟不存在: " + simulationId));

            // 2. 获取当前模拟的所有 Agent
            List<Agent> agents = agentRepo.findBySimulationId(simulationId);
            List<String> allCommands = new ArrayList<>();

            // 3. 遍历每个 Agent，生成 JSON 指令
            for (Agent agent : agents) {
                // 获取该 Agent 的最新对话内容
                List<AgentDialogue> dialogues = agentDialogueRepo.findTopByAgentIdOrderByTimestampDesc(agent.getAgentId());
                String latestDialogue = dialogues.stream()
                        .findFirst()
                        .map(AgentDialogue::getContent)
                        .orElse("");

                // 动态生成 JSON 指令
                String jsonCommand = buildJsonCommand(agent.getRoleType(), latestDialogue);
                allCommands.add(jsonCommand);

                // 4. 保存指令到数据库
                AgentCommand command = new AgentCommand();
                command.setAction(extractAction(jsonCommand)); // 提取动作类型
                command.setParameters(jsonCommand); // 保存完整 JSON 指令
                command.setTimestamp(LocalDateTime.now());
                command.setSimulation(simulation);
                agentCommandRepo.save(command);
            }

            // 5. 构造 JSON 响应
            String jsonResponse = "{\"commands\": [" +
                    allCommands.stream()
                            .map(cmd -> "\"" + cmd.replace("\"", "\\\"") + "\"")
                            .collect(Collectors.joining(",")) +
                    "]}";

            // 6. 构建 WebSocket 消息
            Message message = new Message();
            message.setType("json-command");
            message.setSimulationId(simulationId);
            message.setContent("JSON 指令生成成功");
            message.setData(Map.of(
                    "commands", allCommands // 发送完整的指令列表
            ));

            // 7. 通过 WebSocket 广播消息
            webSocketHandler.broadcastMessage(message);

            // 8. 返回 JSON 响应
            return jsonResponse;
        } catch (Exception e) {
            // 错误处理
            Message errorMessage = new Message();
            errorMessage.setType("error");
            errorMessage.setSimulationId(simulationId);
            errorMessage.setContent("生成 JSON 指令失败");
            errorMessage.setData(Map.of(
                    "reason", e.getMessage()
            ));
            webSocketHandler.broadcastMessage(errorMessage);

            throw new RuntimeException("生成 JSON 指令失败: " + e.getMessage());
        }
    }

    private String parseAnimationType(String jsonCommand) {
        // 示例解析逻辑
        if(jsonCommand.contains("irrigate")) return "watering";
        if(jsonCommand.contains("fertilize")) return "spreading";
        return "default";
    }

    public String startAutoDialogue(String simulationId) {
        try {
            // 1. 获取模拟的环境数据
            Environment env = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);
            List<Crop> crops = cropRepo.findBySimulationId(simulationId);

            // 2. 构建对话上下文
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content",
                    "你是一个农业专家，请根据当前环境讨论下一步计划。当前环境：" + buildEnvironmentSummary(env, crops)));

            // 3. 遍历每个 Agent 生成对话
            List<Agent> agents = agentRepo.findBySimulationId(simulationId);
            StringBuilder dialogueLog = new StringBuilder();
            for (Agent agent : agents) {
                // 3.1 生成角色特定的提示
                String rolePrompt = buildRolePrompt(agent.getRoleType(), messages);
                messages.add(Map.of("role", "user", "content", rolePrompt));

                // 3.2 调用大模型生成回复
                String response = glmService.getResponseFromGLM(messages);
                dialogueLog.append(agent.getAgentName()).append(": ").append(response).append("\n");

                // 3.3 推送对话到 Unity
                Message message = new Message();
                message.setType("auto-dialogue");
                message.setSimulationId(simulationId);
                message.setData(Map.of(
                        "agentId", agent.getAgentId(),
                        "content", response
                ));
                webSocketHandler.broadcastMessage(message);

                // 3.4 更新对话上下文
                messages.add(Map.of("role", "assistant", "content", response));
            }
            return dialogueLog.toString();
        } catch (Exception e) {
            throw new RuntimeException("自动对话失败: " + e.getMessage());
        }
    }

    private String extractAction(String jsonCommand) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonCommand);
            return rootNode.path("action").asText();
        } catch (Exception e) {
            throw new RuntimeException("解析 JSON 指令失败: " + e.getMessage());
        }
    }

    public void optimizeIrrigation(String simulationId) {
        Environment env = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);
        List<Crop> crops = cropRepo.findBySimulationId(simulationId);

        for (Crop crop : crops) {
            double requiredWater = crop.getWaterWeight() * 100; // 假设作物需水量为 waterWeight * 100
            double currentWater = env.getPrecipitation();

            if (currentWater < requiredWater) {
                double irrigationAmount = requiredWater - currentWater;
                applyIrrigation(simulationId, crop.getCropId(), irrigationAmount);
            }
        }
    }

    private void applyIrrigation(String simulationId, String cropId, double amount) {
        // 更新环境数据
        Environment env = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);
        env.setPrecipitation(env.getPrecipitation() + amount);
        environmentRepo.save(env);

        // 记录优化操作
        recordOptimizationAction(simulationId, "irrigation", cropId, amount);
    }


    /**
     * 构建角色提示（不包含 JSON 指令）
     */
    private String buildRolePrompt(String roleType, List<Map<String, String>> messages) {
        String lastMessage = messages.get(messages.size() - 1).get("content");

        // 根据角色类型定制提示词
        String roleSpecificPrompt;
        switch (roleType) {
            case "FARMER":
                roleSpecificPrompt = "你是一个农民，负责作物种植和管理。请根据当前环境和作物情况，提供种植建议。";
                break;
            case "METEOROLOGIST":
                roleSpecificPrompt = "你是一个气象专家，负责分析天气对农业的影响。请根据当前天气情况，提供建议。";
                break;
            case "AGRONOMIST":
                roleSpecificPrompt = "你是一个农业专家，负责提供种植技术和肥料建议。请根据当前土壤和作物情况，提供建议。";
                break;
            default:
                roleSpecificPrompt = "你是一个农业专家，请根据以下问题提供建议。";
                break;
        }

        return String.format("%s\n【最新发言】%s\n你的回应：", roleSpecificPrompt, lastMessage);
    }

    public void triggerEnvironmentResponse(String simulationId, Environment env) {
        List<Agent> agents = agentRepo.findBySimulationId(simulationId);
        for (Agent agent : agents) {
            String prompt = String.format("环境已变化：温度 %.1f℃，土壤肥力 %.0f%%。请提出应对措施。",
                    env.getTemperature(), env.getSoilFertility() * 100);
            String response = glmService.getResponseFromGLM(List.of(
                    Map.of("role", "user", "content", prompt)
            ));

            // 推送消息到 Unity
            Message message = new Message();
            message.setType("environment-response");
            message.setSimulationId(simulationId);
            message.setData(Map.of(
                    "agentId", agent.getAgentId(),
                    "content", response
            ));
            webSocketHandler.broadcastMessage(message);
        }
    }

    /**
     * 动态生成 JSON 指令（基于大模型解析对话内容）
     */
    public String buildJsonCommand(String roleType, String dialogueContent) {
        try {
            // 1. 构建大模型提示词（明确要求生成 JSON）
            String systemPrompt = buildSystemPromptForJsonGeneration(roleType);

            // 2. 调用大模型解析对话内容
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", systemPrompt));
            messages.add(Map.of("role", "user", "content", dialogueContent));
            String rawResponse = glmService.getResponseFromGLM(messages);

            // 3. 提取 JSON 部分（兼容 ||JSON_START|| 标记或纯 JSON）
            String jsonString = extractJsonFromResponse(rawResponse);

            // 4. 验证 JSON 格式和必填字段
            return validateAndFormatJson(jsonString);
        } catch (Exception e) {
            return "{\"error\":\"生成指令失败: " + e.getMessage() + "\"}";
        }
    }


    private String buildSystemPromptForJsonGeneration(String roleType) {
        return "你是一个农业决策生成器，请根据对话内容生成一个 JSON 指令。\n" +
                "要求：\n" +
                "1. 仅返回 JSON，不要包含任何自然语言解释。\n" +
                "2. 使用以下格式标记 JSON：||JSON_START|| <你的JSON> ||JSON_END||\n" +
                "3. 指令类型参考：\n" +
                "   - 农民：灌溉(irrigate)、种植(plant)、收割(harvest)\n" +
                "   - 气象专家：天气预警(alert)、农时建议(suggest_schedule)\n" +
                "   - 农业专家：施肥(fertilize)、病害诊断(diagnose)\n" +
                "示例：\n" +
                "||JSON_START||\n" +
                "{\"action\":\"irrigate\", \"target\":\"field-1\", \"parameters\":{\"duration\":2, \"waterAmount\":500}}\n" +
                "||JSON_END||";
    }



    /**
     * 从大模型响应中提取 JSON
     */
    private String extractJsonFromResponse(String rawResponse) {
        // 匹配 ||JSON_START||...||JSON_END||
        Pattern pattern = Pattern.compile("\\|\\|JSON_START\\|\\|([\\s\\S]*?)\\|\\|JSON_END\\|\\|");
        Matcher matcher = pattern.matcher(rawResponse);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        // 匹配 ```json...```
        pattern = Pattern.compile("```json\\n([\\s\\S]*?)\\n```");
        matcher = pattern.matcher(rawResponse);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }

        // 匹配纯 JSON（无标记）
        try {
            new ObjectMapper().readTree(rawResponse);
            return rawResponse.trim();
        } catch (Exception e) {
            throw new IllegalArgumentException("响应中未找到有效 JSON");
        }
    }

    private void recordOptimizationAction(String simulationId, String actionType, String target, double amount) {
        OptimizationAction action = new OptimizationAction();
        action.setSimulationId(simulationId);
        action.setActionType(actionType);
        action.setTarget(target);
        action.setAmount(amount);
        action.setTimestamp(LocalDateTime.now());
        optimizationActionRepo.save(action);
    }

    /**
     * 验证 JSON 格式并补全必填字段
     */
    private String validateAndFormatJson(String jsonString) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(jsonString);

            // 必填字段检查
            if (!rootNode.has("action")) {
                throw new IllegalArgumentException("Missing required field: action");
            }

            // 默认补全字段
            ObjectNode jsonNode = (ObjectNode) rootNode;
            if (!jsonNode.has("timestamp")) {
                jsonNode.put("timestamp", LocalDateTime.now().toString());
            }

            return mapper.writeValueAsString(jsonNode);
        } catch (Exception e) {
            return "{\"error\":\"Invalid JSON: " + e.getMessage() + "\"}";
        }
    }

    private void saveDialogue(String simulationId, String agentId, String role, String content) {
        AgentDialogue dialogue = new AgentDialogue();
        dialogue.setAgentId(agentId);
        dialogue.setRole(role);
        dialogue.setContent(content);
        dialogue.setTimestamp(LocalDateTime.now());

        Simulation simulation = simulationRepo.findById(simulationId)
                .orElseThrow(() -> new RuntimeException("模拟不存在: " + simulationId));
        dialogue.setSimulation(simulation);

        agentDialogueRepo.save(dialogue);
    }

    @Transactional
    public void createAgent(String agentName, String simulationId) {
        try {
        Agent agent = new Agent();
        agent.setAgentId("AGENT-" + System.currentTimeMillis());
        agent.setAgentName(agentName);
        agent.setRoleType("FARMER");
        agent.setPlantingSkill(0.3 + Math.random() * 0.4);
        agent.setLearningAbility(0.3 + Math.random() * 0.4);
        agent.setLocalKnowledge(0.3 + Math.random() * 0.4);

        Simulation simulation = simulationRepo.findById(simulationId)
                .orElseThrow(() -> new RuntimeException("模拟不存在: " + simulationId));
        agent.setSimulation(simulation);


        // 根据本地知识确定可访问的数据类型
        List<String> accessibleData = new ArrayList<>();
        if (agent.getLocalKnowledge() > 0.5) accessibleData.add("temperature");
        if (agent.getLocalKnowledge() > 0.6) accessibleData.add("soil");
        agent.setAccessibleData(accessibleData);

        agentRepo.save(agent);

        Message message = new Message();
        message.setType("agent-created");
        message.setSimulationId(simulationId);
            message.setData(Map.of(
                    "agentId", agent.getAgentId(),
                    "agentName", agent.getAgentName(),
                    "roleType", agent.getRoleType(),
                    "position", Map.of("x", -0.45, "y", 0.32, "z", 0.1)
            ));
        webSocketHandler.broadcastMessage(message);
        } catch (Exception e) {
            throw new RuntimeException("创建Agent失败: " + e.getMessage());
        }
    }


    public Map<String, Object> getGlobalData(String simulationId) {
        Map<String, Object> data = new HashMap<>();
        data.put("environment", environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId));
        data.put("crops", cropRepo.findBySimulationId(simulationId));
        data.put("agents", agentRepo.findBySimulationId(simulationId));
        return data;
    }

    public void optimizeFertilization(String simulationId) {
        Environment env = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);
        List<Crop> crops = cropRepo.findBySimulationId(simulationId);

        for (Crop crop : crops) {
            double requiredFertility = crop.getSoilWeight() * 0.8; // 假设作物需求为 soilWeight * 0.8
            double currentFertility = env.getSoilFertility();

            if (currentFertility < requiredFertility) {
                double fertilizerAmount = requiredFertility - currentFertility;
                applyFertilizer(simulationId, crop.getCropId(), fertilizerAmount);
            }
        }
    }


    private void applyFertilizer(String simulationId, String cropId, double amount) {
        // 更新环境数据
        Environment env = environmentRepo.findTopBySimulationIdOrderByTimestampDesc(simulationId);
        env.setSoilFertility(env.getSoilFertility() + amount);
        environmentRepo.save(env);

        // 记录优化操作
        recordOptimizationAction(simulationId, "fertilization", cropId, amount);
    }

    public void optimizeResourceAllocation(String simulationId) {

        optimizeIrrigation(simulationId);
        optimizeFertilization(simulationId);
        // 1. 获取全局数据
        Map<String, Object> globalData = getGlobalData(simulationId);
        List<Agent> farmers = (List<Agent>) globalData.get("agents");
        List<Crop> crops = (List<Crop>) globalData.get("crops");

        // 2. 根据农民能力分配作物
        farmers.sort((a, b) -> Double.compare(b.getPlantingSkill(), a.getPlantingSkill()));
        crops.sort((a, b) -> Double.compare(b.getGrowthRate(), a.getGrowthRate()));

        // 3. 高技能农民负责高价值作物
        for (int i = 0; i < Math.min(farmers.size(), crops.size()); i++) {
            assignCropToFarmer(farmers.get(i).getAgentId(), crops.get(i).getCropId());
        }
    }

    public void triggerAgentDialogue(String simulationId) {
        List<Agent> agents = agentRepo.findBySimulationId(simulationId);
        List<Agent> farmers = agents.stream()
                .filter(a -> a.getRoleType().equals("FARMER"))
                .collect(Collectors.toList());
        Optional<Agent> expert = agents.stream()
                .filter(a -> a.getRoleType().equals("AGRONOMIST"))
                .findFirst();

        // 农民之间对话
        farmers.forEach(farmer -> {
            String farmerDialogue = generateFarmerDialogue(farmer);
            sendDialogueMessage(simulationId, farmer.getAgentId(), farmerDialogue);
        });

        // 专家指导农民
        expert.ifPresent(e -> {
            farmers.forEach(farmer -> {
                String advice = generateExpertAdvice(e, farmer);
                sendDialogueMessage(simulationId, e.getAgentId(), advice);
            });
        });
    }

    private String generateFarmerDialogue(Agent farmer) {
        // 根据农民的知识生成对话
        return String.format("农民 %s: 我注意到土壤湿度是 %.1f%%，可能需要调整灌溉。",
                farmer.getAgentName(), farmer.getLocalKnowledge() * 100);
    }

    private String generateExpertAdvice(Agent expert, Agent farmer) {
        // 生成专家建议
        return String.format("专家 %s ➔ 农民 %s: 根据你的地块数据，建议增加10%%的施肥量。",
                expert.getAgentName(), farmer.getAgentName());
    }

    private void sendDialogueMessage(String simId, String agentId, String content) {
        Message message = new Message();
        message.setType("agent-dialogue");
        message.setSimulationId(simId);
        message.setData(Map.of(
                "agentId", agentId,
                "content", content,
                "timestamp", LocalDateTime.now().toString()
        ));
        webSocketHandler.broadcastMessage(message);
    }

    private void assignCropToFarmer(String farmerId, String cropId) {
        // 更新数据库或发送指令到Agent
        System.out.println("分配作物 " + cropId + " 给农民 " + farmerId);
    }

    // AgentService.java
    public void createAgronomist(String simulationId) {
        // 创建专家Agent
        Agent expert = new Agent();
        expert.setAgentId("EXPERT-" + System.currentTimeMillis());
        expert.setAgentName("农业专家-" + (int)(Math.random() * 1000)); // 随机生成默认名称
        expert.setRoleType("AGRONOMIST");
        expert.setPlantingSkill(1.0);
        expert.setLearningAbility(1.0);
        expert.setLocalKnowledge(1.0);
        expert.setAccessibleData(List.of("temperature", "soil", "crops", "agents"));

        Simulation simulation = simulationRepo.findById(simulationId)
                .orElseThrow(() -> new RuntimeException("模拟不存在: " + simulationId));
        expert.setSimulation(simulation);

        agentRepo.save(expert);
        System.out.println("农业专家创建成功：" + expert.getAgentName());
    }
    public String makeDecision(String agentId, String prompt) {
        try {
            // 1. 查找 Agent
            Agent agent = agentRepo.findById(agentId)
                    .orElseThrow(() -> new RuntimeException("Agent 不存在: " + agentId));

            // 2. 构建对话上下文
            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "system", "content", "你是一个农业专家，请根据以下问题提供建议。"));
            messages.add(Map.of("role", "user", "content", prompt));

            // 3. 调用大模型生成回复
            String response = glmService.getResponseFromGLM(messages);

            // 4. 保存对话记录
            saveDialogue(agent.getSimulation().getId(), agentId, agent.getRoleType(), response);

            // 5. 构建 WebSocket 消息
            Message message = new Message();
            message.setType("agent-decision");
            message.setContent("Agent 决策完成");
            message.setSimulationId(agent.getSimulation().getId());
            message.setData(Map.of(
                    "agentId", agent.getAgentId(),
                    "agentName", agent.getAgentName(),
                    "decision", response
            ));

            // 6. 通过 WebSocket 发送消息
            webSocketHandler.broadcastMessage(message);

            // 7. 返回生成的回复
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Agent 决策失败: " + e.getMessage());
        }
    }
}