export default {
  path: "/unity",
  meta: {
    title: "实时画面"
  },
  children: [
    {
      path: "/unity/index",
      name: "Unity",
      component: () => import("@/views/unity/index.vue"),
      meta: {
        title: "实时画面",
        showParent: true
      }
    }
  ]
};
