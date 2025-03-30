using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;

public class thefarmer : MonoBehaviour
{
    public GameObject weatherSystem;
    public float delayTime = 3.0f;
    public Vector3 targetPosition = new Vector3(-19, 0, 31);
    void Start()
    {
        //Invoke("kguan", delayTime);
        transform.position = targetPosition;
    }

    // Update is called once per frame
    void Update()
    {
        
    }
    private void OnCollisionEnter(Collision collision)
    {
        kguan();
    }


    void kguan()
    {
        
        if (!weatherSystem.activeSelf)
        {
            weatherSystem.SetActive(true);
        }
        else if (weatherSystem.activeSelf)
        {
           weatherSystem.SetActive(false);
        }
    }
    
    
}
