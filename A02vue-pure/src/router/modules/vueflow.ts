import { vueflow } from "@/router/enums";

export default {
  path: "/vue-flow",
  redirect: "/vue-flow/index",
  meta: {
    icon: "ep:set-up",
    title: "快速查看状态",
    rank: vueflow
  },
  children: [
    {
      path: "/vue-flow/index",
      name: "VueFlow",
      component: () => import("@/views/vue-flow/layouting/index.vue"),
      meta: {
        title: "快速查看状态"
      }
    }
  ]
} satisfies RouteConfigsTable;
