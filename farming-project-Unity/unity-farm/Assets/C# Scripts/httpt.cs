using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Newtonsoft.Json;
using static httpt;


//  cs���ʾjson���ݽṹ


public class httpt : MonoBehaviour
{
   
    // ����JSON�ַ���
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
        // ��JSON�ַ��������л�ΪModelData����
        ModelData modelData = JsonConvert.DeserializeObject<ModelData>(jsonString);

        // ����modelData����ģ��
        GenerateModel(modelData);
    }

    public void ReceiveJson_two(string jsonString)
    {
        Modelmove modlemove = JsonConvert.DeserializeObject<Modelmove>(jsonString);
        Move(modlemove);
    }

    private void GenerateModel(ModelData modelData)
    {
        // ����modelName����ģ��Ԥ����   �ص� �ص� �ص� !!!!!!!!!!!!!!!!!!!!
        GameObject prefab = Resources.Load<GameObject>(modelData.modelName);
        if (prefab == null)
        {
            Debug.LogError($"Prefab '{modelData.modelName}' not found in Resources folder.");
            return; 
        }

        GameObject modelInstance = Instantiate(prefab);

        // ����ģ�͵�λ�á���ת������
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






