using UnityEngine;

public class ObjectPositioner : MonoBehaviour
{
    public LayerMask groundLayer; // 地面层

    void Start()
    {
        RaycastHit hit;
        if (Physics.Raycast(transform.position, Vector3.down, out hit, Mathf.Infinity, groundLayer))
        {
            // 调整物体位置，使其底部与地面接触
            transform.position = new Vector3(transform.position.x, hit.point.y, transform.position.z);
        }
    }
}