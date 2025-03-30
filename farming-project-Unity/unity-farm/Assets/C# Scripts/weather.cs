using System.Collections;
using System.Collections.Generic;
using UnityEngine;


public class weather : MonoBehaviour
{
    public float delayTime = 3.0f;
    // Start is called before the first frame update
    void Start()
    {
        Invoke("trueorf", delayTime);
    }

    // Update is called once per frame
    void Update()
    {
        
    }
    void DisableObject()
    {
        // 禁用当前物体
        gameObject.SetActive(false);
    }

     void trueorf()
    {
        if (!gameObject.activeSelf)
        {
            gameObject.SetActive(true);
        }
        else if (gameObject.activeSelf)
        {
            gameObject.SetActive(false);
        }
        
    }
}
