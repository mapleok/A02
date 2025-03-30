import { $t } from "@/plugins/i18n";
import { markdown } from "@/router/enums";

export default {
  path: "/markdown",
  redirect: "/markdown/index",
  meta: {
    icon: "ri:markdown-line",
    title: $t("menus.pureMarkdown"),
    showLink: false, // 不显示在侧边栏
    rank: markdown
  },
  children: [
    {
      path: "/markdown/index",
      name: "Markdown",
      component: () => import("@/views/markdown/index.vue"),
      meta: {
        title: $t("menus.pureMarkdown"),
        extraIcon: "IF-pure-iconfont-new svg"
      }
    }
  ]
} satisfies RouteConfigsTable;
