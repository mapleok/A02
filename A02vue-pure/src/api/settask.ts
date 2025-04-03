// src/api/dialogue.ts
import api from "./request";

// 启动对话
export const startDialogue = (prompt: string) => {
  return api.post("/start-dialogue", { prompt });
};
