using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class CamMove : MonoBehaviour
{
    public GameObject[] cameras;
    public string[] shotcuts;
    public bool changeAudioListener = true;
    // Start is called before the first frame update
    void Start()
    {

    }

    // Update is called once per frame
    void Update()
    {
        int i = 0;
        for (i = 0; i < cameras.Length; i++)
        {
            if (Input.GetKeyUp(shotcuts[i]))
            {
                SwitchCamera(i);
            }
        }
    }

    void SwitchCamera(int index)
    {
        int i = 0;
        for (i = 0; i < cameras.Length; i++)
        {
            if (i != index)
            {
                if (changeAudioListener)
                {
                    cameras[i].GetComponent<AudioSource>().enabled = false;
                }
                cameras[i].GetComponent<Camera>().enabled = false;
            }
            else
            {
                if (changeAudioListener)
                {
                    cameras[i].GetComponent<AudioSource>().enabled = true;
                }
                cameras[i].GetComponent<Camera>().enabled = true;
            }
        }
    }
}