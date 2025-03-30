using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class walk : MonoBehaviour
{
    public Transform targetPosition; // 目标位置
    public float moveDuration = 6.0f; // 移动持续时间
    public float turnSpeed = 5.0f; // 转向速度
    private float elapsedTime; // 已经过去的时间
    private Vector3 startPosition; // 起始位置
    private bool isTurning = true; // 是否正在转向

    void Start()
    {
        startPosition = transform.position; // 记录起始位置
    }

    void Update()
    {
        if (isTurning)
        {
            // 计算转向目标方向
            Vector3 targetDirection = targetPosition.position - transform.position;
            targetDirection.y = 0; // 忽略Y轴，确保只在水平面上转向

            if (targetDirection != Vector3.zero)
            {
                // 计算目标旋转
                Quaternion targetRotation = Quaternion.LookRotation(targetDirection);
                // 平滑转向
                transform.rotation = Quaternion.Slerp(transform.rotation, targetRotation, turnSpeed * Time.deltaTime);

                // 判断是否接近目标旋转
                if (Quaternion.Angle(transform.rotation, targetRotation) < 1f)
                {
                    isTurning = false; // 转向完成，开始移动
                    elapsedTime = 0; // 重置移动时间
                }
            }
        }
        else
        {
            if (elapsedTime < moveDuration)
            {
                // 插值计算当前位置
                transform.position = Vector3.Lerp(startPosition, targetPosition.position, elapsedTime / moveDuration);
                elapsedTime += Time.deltaTime; // 累加时间
            }
            else
            {
                // 确保最终到达目标位置
                transform.position = targetPosition.position;
            }
        }
    }
}