using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LV : MonoBehaviour
{
    public GameObject LV2;
    public GameObject LV3;
    public float lv2 = 5.0f;
    public float lv3 = 10.0f;
    
    // Start is called before the first frame update
    void Start()
    {
        LV2.SetActive(false);
        LV3.SetActive(false);
        
    }

    // Update is called once per frame
    void Update()
    {
        Invoke("twotwo", lv2);
        Invoke("threethree", lv3);
    }


    void twotwo()
    {
        gameObject.SetActive(false);
        LV2.SetActive(false);
        LV3.SetActive(false);
        if (!gameObject.activeSelf && !LV3.activeSelf)
        {
            LV2.SetActive(true);
        }
    }

    void threethree()
    {
        gameObject.SetActive(false);
        LV2.SetActive(false);
        LV3.SetActive(false);
        if (!gameObject.activeSelf && !LV2.activeSelf)
        {
            LV3.SetActive(true);
        }
    }
}
