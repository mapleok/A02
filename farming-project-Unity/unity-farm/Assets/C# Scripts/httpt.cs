using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Newtonsoft.Json;
using static httpt;


//  cs类表示json数据结构


public class httpt : MonoBehaviour
{
   
    // 接收JSON字符串
    [System.Serializable]
    public class ModelData
    {
        public string modelName;
        public Vector3 position_position;
       
    }


    [System.Serializable]
    public class Modelmove
    {
        
        public string modelName;
        public Vector3 position_position;
        public Vector3 target_target;
    }


    void Start()
    {
        string jsonString = @"
        {
            ""modelName"": ""farmer"",
            ""position_position"": { ""x"": -22, ""y"": 0, ""z"": 13 },
            ""target_target"": { ""x"": -22, ""y"": 0, ""z"": 30 }
        }";
        ReceiveJson_two(jsonString);
        
    }


    public void ReceiveJson(string jsonString)
    {
        // 将JSON字符串反序列化为ModelData对象
        ModelData modelData = JsonConvert.DeserializeObject<ModelData>(jsonString);

        // 根据modelData生成模型
        GenerateModel(modelData);
    }

    public void ReceiveJson_two(string jsonString)
    {
        Modelmove modlemove = JsonConvert.DeserializeObject<Modelmove>(jsonString);
        Move(modlemove);
    }

    private void GenerateModel(ModelData modelData)
    {
        // 根据modelName加载模型预制体   重点 重点 重点 !!!!!!!!!!!!!!!!!!!!
        GameObject prefab = Resources.Load<GameObject>(modelData.modelName);
        if (prefab == null)
        {
            Debug.LogError($"Prefab '{modelData.modelName}' not found in Resources folder.");
            return; 
        }

        GameObject modelInstance = Instantiate(prefab);

        // 设置模型的位置、旋转和缩放
        modelInstance.transform.position = modelData.position_position;
      
    }

    private void Move(Modelmove modelmove)
    {
        GameObject prefab = Resources.Load<GameObject>(modelmove.modelName);
        if (prefab == null)
        {
            Debug.LogError($"Prefab '{modelmove.modelName}' not found in Resources folder.");
            return;
        }

        GameObject modelInstance = Instantiate(prefab);

        modelInstance.transform.position = modelmove.position_position;
        Vector3 startposition = modelmove.position_position;
        Invoke("moveychi", 4.0f);
        
        void moveychi()
        {
            modelInstance.transform.position = Vector3.Lerp(startposition, modelmove.target_target, 4.0f);
        }
    }

    
}






