using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class camera_c : MonoBehaviour
{
   
    float movespeed = 50;
    public Camera camera;
    // Start is called before the first frame update
    void Start()
    {
        camera = GetComponent<Camera>();
    }

    // Update is called once per frame
    void Update()
    {
        Move();
    }

    void Move()
    {
        //×óÓÒ
        float h = Input.GetAxis("Horizontal");
        //Ç°ºó
        float v = Input.GetAxis("Vertical");

        Vector3 dir = new Vector3(-v, 0, h);

        transform.position += dir * Time.deltaTime * movespeed;

       
        float mouseScrollWheel = Input.GetAxis("Mouse ScrollWheel");

        if (mouseScrollWheel > 0)
        {
            camera.fieldOfView += mouseScrollWheel * 2 * 5;
        }
        else if (mouseScrollWheel < 0 )
        {
            camera.fieldOfView -= mouseScrollWheel * -2 * 5;
        }
    }
}
