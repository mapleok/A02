using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class walk : MonoBehaviour
{
    public Transform targetPosition; // Ŀ��λ��
    public float moveDuration = 6.0f; // �ƶ�����ʱ��
    public float turnSpeed = 5.0f; // ת���ٶ�
    private float elapsedTime; // �Ѿ���ȥ��ʱ��
    private Vector3 startPosition; // ��ʼλ��
    private bool isTurning = true; // �Ƿ�����ת��

    void Start()
    {
        startPosition = transform.position; // ��¼��ʼλ��
    }

    void Update()
    {
        if (isTurning)
        {
            // ����ת��Ŀ�귽��
            Vector3 targetDirection = targetPosition.position - transform.position;
            targetDirection.y = 0; // ����Y�ᣬȷ��ֻ��ˮƽ����ת��

            if (targetDirection != Vector3.zero)
            {
                // ����Ŀ����ת
                Quaternion targetRotation = Quaternion.LookRotation(targetDirection);
                // ƽ��ת��
                transform.rotation = Quaternion.Slerp(transform.rotation, targetRotation, turnSpeed * Time.deltaTime);

                // �ж��Ƿ�ӽ�Ŀ����ת
                if (Quaternion.Angle(transform.rotation, targetRotation) < 1f)
                {
                    isTurning = false; // ת����ɣ���ʼ�ƶ�
                    elapsedTime = 0; // �����ƶ�ʱ��
                }
            }
        }
        else
        {
            if (elapsedTime < moveDuration)
            {
                // ��ֵ���㵱ǰλ��
                transform.position = Vector3.Lerp(startPosition, targetPosition.position, elapsedTime / moveDuration);
                elapsedTime += Time.deltaTime; // �ۼ�ʱ��
            }
            else
            {
                // ȷ�����յ���Ŀ��λ��
                transform.position = targetPosition.position;
            }
        }
    }
}