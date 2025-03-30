using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.AI;
using UnityEngine.UI;

public class model_move : MonoBehaviour
{
    private NavMeshAgent nav;
    public Vector3 destination;
    // Start is called before the first frame update
    void Start()
    {
        GoDestination("plant-salad (193)");

    }

    // Update is called once per frame
    public void GoDestination(string desname)
    {
        nav = this.GetComponent<NavMeshAgent>();
        GameObject vegetable = GameObject.Find(desname);
        destination = vegetable.transform.position;
        Debug.Log("Ä¿µÄµØ" + destination);
        nav.SetDestination(destination);
    }
}
