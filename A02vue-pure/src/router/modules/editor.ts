import { $t } from "@/plugins/i18n";
import { editor } from "@/router/enums";

export default {
  path: "/editor",
  redirect: "/editor/index",
  meta: {
    icon: "ep:edit",
    // showLink: false,
    title: $t("menus.pureEditor"),
    rank: editor
  },
  children: [
    {
      path: "/editor/index",
      name: "Editor",
      component: () => import("@/views/editor/index.vue"),
      meta: {
        title: $t("menus.pureEditor"),
        keepAlive: true
      }
    },
    {
      path: "/about/index",
      name: "About",
      component: () => import("@/views/about/index.vue"),
      meta: {
        title: $t("menus.pureAbout")
      }
    },
    {
      path: "/guide/index",
      name: "Guide",
      component: () => import("@/views/guide/index.vue"),
      meta: {
        title: $t("menus.pureGuide")
      }
    },
    {
      path: "/menuoverflow/index",
      name: "MenuOverflow",
      component: () => import("@/views/menuoverflow/index.vue"),
      meta: {
        title: $t("menus.pureChildMenuOverflow"),
        showParent: true
      }
    },
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
