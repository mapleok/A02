using UnityEngine;

public class ObjectPositioner : MonoBehaviour
{
    public LayerMask groundLayer; // �����

    void Start()
    {
        RaycastHit hit;
        if (Physics.Raycast(transform.position, Vector3.down, out hit, Mathf.Infinity, groundLayer))
        {
            // ��������λ�ã�ʹ��ײ������Ӵ�
            transform.position = new Vector3(transform.position.x, hit.point.y, transform.position.z);
        }
    }
}