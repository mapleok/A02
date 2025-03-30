//using System;
//using System.Collections;
//using System.Collections.Generic;
//using UnityEngine;
//using Newtonsoft.Json;




//public class texthttp : MonoBehaviour
//{

//    private int lv = -2;
//    [SerializeField]
//    private GameObject[] lvPrefabs;

//    private GameObject modle;

//    public int Lv { get => lv; 
//        set
//        {
//            if (lv != value)
//            {
//                lv = value;
//                //Ïú»ÙÄ£ÐÍ
//                if (modle == null) Destroy(modle);
//                modle = GameObject.Instantiate(lvPrefabs[Lv], transform);
//            }
//        }
//    }
//    void Start()
//    {
//        Lv = 1;

//        Invoke("uplv", 5.0f);
//        Invoke("uplv", 10.0f);
//    }

//    private void Update()
//    {
        
//    }

//    private void uplv()
//    {
//        Lv++;
//    }

//}






