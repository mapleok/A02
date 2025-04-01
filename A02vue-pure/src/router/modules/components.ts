import { $t } from "@/plugins/i18n";
import {able, components, editor} from "@/router/enums";

export default {
  path: "/components",
  redirect: "/components/dialog",
  meta: {
    icon: "ep:menu",
    title: $t("menus.pureComponents"),
    rank: components
  },
  children: [
    {
      path: "/editor",
      redirect: "/editor/index",
      meta: {
        icon: "ep:edit",
        // showLink: false,
        title: $t("menus.pureEditor")
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
    },
    {
      path: "/ganttastic/index",
      name: "Ganttastic",
      component: () => import("@/views/ganttastic/index.vue"),
      meta: {
        icon: "ri:bar-chart-horizontal-line",
        title: $t("menus.pureGanttastic")
      }
    },
    {
      path: "/list/card",
      name: "CardList",
      component: () => import("@/views/list/card/index.vue"),
      meta: {
        icon: "ri:bank-card-line",
        title: $t("menus.pureCardList"),
        showParent: true
      }
    },
    {
      path: "/editor/index",
      name: "Editor",
      component: () => import("@/views/editor/index.vue"),
      meta: {
        title: $t("menus.pureEditor")
      }
    },
    {
      path: "/codemirror/index",
      name: "CodeMirror",
      component: () => import("@/views/codemirror/index.vue"),
      meta: {
        title: $t("menus.pureCodeMirror"),
        extraIcon: "IF-pure-iconfont-new svg"
      }
    },
    {
      path: "/flow-chat/index",
      name: "FlowChart",
      component: () => import("@/views/flow-chart/index.vue"),
      meta: {
        title: $t("menus.pureFlowChart")
      }
    },
    {
      path: "/mind-map/index",
      name: "MindMap",
      component: () => import("@/layout/frame.vue"),
      meta: {
        title: $t("menus.pureMindMap"),
        frameSrc: "https://www.wangling2.github.io/mind-map/#/",
        extraIcon: "IF-pure-iconfont-new svg"
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
      path: "/board/index",
      name: "FrameBoard",
      component: () => import("@/layout/frame.vue"),
      meta: {
        title: $t("menus.pureBoard"),
        frameSrc: "https://songlh.top/paint-board/"
      }
    },
    // {
    //   path: "/iframe"
    //   name: "FrameBoard",
    //   component: () => import("@/layout/iframe.vue"),
    //   meta: {
    //     title: $t("menus.pureBoard"),
    //     frameSrc: "https://songlh.top/paint-board/"
    //   }
    // },
    {
      path: "/able",
      redirect: "/able/watermark",
      meta: {
        icon: "ri:ubuntu-fill",
        title: $t("menus.pureAble"),
      },
      children: [
        {
          path: "/able/mqtt-client",
          name: "MqttClient",
          component: () => import("@/views/able/mqtt-client.vue"),
          meta: {
            title: $t("menus.pureMqtt")
          }
        },
        {
          path: "/able/verify",
          name: "Verify",
          component: () => import("@/views/able/verify.vue"),
          meta: {
            title: $t("menus.pureVerify")
          }
        },
        {
          path: "/able/watermark",
          name: "WaterMark",
          component: () => import("@/views/able/watermark.vue"),
          meta: {
            title: $t("menus.pureWatermark")
          }
        },
        {
          path: "/able/print",
          name: "Print",
          component: () => import("@/views/able/print/index.vue"),
          meta: {
            title: $t("menus.purePrint")
          }
        },
        {
          path: "/able/download",
          name: "Download",
          component: () => import("@/views/able/download.vue"),
          meta: {
            title: $t("menus.pureDownload")
          }
        },
        {
          path: "/able/excel",
          name: "Excel",
          component: () => import("@/views/able/excel.vue"),
          meta: {
            title: $t("menus.pureExcel")
          }
        },
        {
          path: "/components/ripple",
          name: "Ripple",
          component: () => import("@/views/able/ripple.vue"),
          meta: {
            title: $t("menus.pureRipple")
          }
        },
        {
          path: "/able/debounce",
          name: "Debounce",
          component: () => import("@/views/able/debounce.vue"),
          meta: {
            title: $t("menus.pureDebounce")
          }
        },
        {
          path: "/able/directives",
          name: "Directives",
          component: () => import("@/views/able/directives.vue"),
          meta: {
            title: $t("menus.pureOptimize")
          }
        },
        {
          path: "/able/draggable",
          name: "Draggable",
          component: () => import("@/views/able/draggable.vue"),
          meta: {
            title: $t("menus.pureDraggable"),
            transition: {
              enterTransition: "animate__zoomIn",
              leaveTransition: "animate__zoomOut"
            }
          }
        },
        {
          path: "/able/pdf",
          name: "Pdf",
          component: () => import("@/views/able/pdf.vue"),
          meta: {
            title: $t("menus.purePdf")
          }
        },
        {
          path: "/able/barcode",
          name: "BarCode",
          component: () => import("@/views/able/barcode.vue"),
          meta: {
            title: $t("menus.pureBarcode")
          }
        },
        {
          path: "/able/qrcode",
          name: "QrCode",
          component: () => import("@/views/able/qrcode.vue"),
          meta: {
            title: $t("menus.pureQrcode")
          }
        },
        {
          path: "/able/map",
          name: "MapPage",
          component: () => import("@/views/able/map.vue"),
          meta: {
            title: $t("menus.pureMap"),
            keepAlive: true,
            transition: {
              name: "fade"
            }
          }
        },
        {
          path: "/able/wavesurfer",
          name: "Wavesurfer",
          component: () => import("@/views/able/wavesurfer/index.vue"),
          meta: {
            title: $t("menus.pureWavesurfer")
          }
        },
        {
          path: "/able/video",
          name: "VideoPage",
          component: () => import("@/views/able/video.vue"),
          meta: {
            title: $t("menus.pureVideo")
          }
        },
        {
          path: "/able/video-frame",
          name: "VideoFrame",
          component: () => import("@/views/able/video-frame/index.vue"),
          meta: {
            title: $t("menus.pureVideoFrame")
          }
        },
        {
          path: "/able/danmaku",
          name: "Danmaku",
          component: () => import("@/views/able/danmaku/index.vue"),
          meta: {
            title: $t("menus.pureDanmaku")
          }
        },
        {
          path: "/able/infinite-scroll",
          name: "InfiniteScroll",
          component: () => import("@/views/able/infinite-scroll.vue"),
          meta: {
            title: $t("menus.pureInfiniteScroll")
          }
        },
        {
          path: "/able/menu-tree",
          name: "MenuTree",
          component: () => import("@/views/able/menu-tree.vue"),
          meta: {
            title: $t("menus.pureMenuTree")
          }
        },
        {
          path: "/able/line-tree",
          name: "LineTree",
          component: () => import("@/views/able/line-tree.vue"),
          meta: {
            title: $t("menus.pureLineTree")
          }
        },
        {
          path: "/able/typeit",
          name: "Typeit",
          component: () => import("@/views/able/typeit.vue"),
          meta: {
            title: $t("menus.pureTypeit")
          }
        },
        {
          path: "/able/sensitive",
          name: "Sensitive",
          component: () => import("@/views/able/sensitive.vue"),
          meta: {
            title: $t("menus.pureSensitive")
          }
        },
        {
          path: "/able/pinyin",
          name: "Pinyin",
          component: () => import("@/views/able/pinyin.vue"),
          meta: {
            title: $t("menus.purePinyin")
          }
        }
      ]
    },
    {
      path: "/nested",
      redirect: "/nested/menu1/menu1-1",
      meta: {
        title: $t("menus.pureMenus"),
        icon: "ep:histogram"
      },
      children: [
        {
          path: "/nested/menu1",
          meta: {
            title: $t("menus.pureMenu1"),
            keepAlive: true
          },
          redirect: "/nested/menu1/menu1-1",
          children: [
            {
              path: "/nested/menu1/menu1-1",
              component: () => import("@/views/nested/menu1/menu1-1/index.vue"),
              name: "Menu1-1",
              meta: {
                title: $t("menus.pureMenu1-1"),
                keepAlive: true
              }
            },
            {
              path: "/nested/menu1/menu1-2",
              redirect: "/nested/menu1/menu1-2/menu1-2-1",
              meta: {
                title: $t("menus.pureMenu1-2"),
                keepAlive: true
              },
              children: [
                {
                  path: "/nested/menu1/menu1-2/menu1-2-1",
                  component: () =>
                    import("@/views/nested/menu1/menu1-2/menu1-2-1/index.vue"),
                  name: "Menu1-2-1",
                  meta: {
                    title: $t("menus.pureMenu1-2-1"),
                    keepAlive: true
                  }
                },
                {
                  path: "/nested/menu1/menu1-2/menu1-2-2",
                  component: () =>
                    import("@/views/nested/menu1/menu1-2/menu1-2-2/index.vue"),
                  name: "Menu1-2-2",
                  meta: {
                    title: $t("menus.pureMenu1-2-2"),
                    keepAlive: true
                  }
                }
              ]
            },
            {
              path: "/nested/menu1/menu1-3",
              component: () => import("@/views/nested/menu1/menu1-3/index.vue"),
              name: "Menu1-3",
              meta: {
                title: $t("menus.pureMenu1-3"),
                keepAlive: true
              }
            }
          ]
        }
      ]
    },
    {
      path: "/form-design/index",
      name: "FormDesign",
      component: () => import("@/layout/frame.vue"),
      meta: {
        title: $t("menus.pureFormDesign"),
        keepAlive: true,
        frameSrc:
          "https://haixin-fang.github.io/vue-form-design/playground/index.html",
        frameLoading: false
      }
    },
    {
      path: "/ppt/index",
      name: "FramePpt",
      component: () => import("@/layout/frame.vue"),
      meta: {
        title: "PPT",
        keepAlive: true,
        frameSrc: "https://pipipi-pikachu.github.io/PPTist/",
        frameLoading: false
      }
    },
    {
      path: "/components/dialog",
      name: "DialogPage",
      component: () => import("@/views/components/dialog/index.vue"),
      meta: {
        title: $t("menus.pureDialog")
      }
    },
    {
      path: "/components/drawer",
      name: "DrawerPage",
      component: () => import("@/views/components/drawer/index.vue"),
      meta: {
        title: $t("menus.pureDrawer")
      }
    },
    {
      path: "/components/message",
      name: "Message",
      component: () => import("@/views/components/message.vue"),
      meta: {
        title: $t("menus.pureMessage")
      }
    },
    {
      path: "/components/upload",
      name: "PureUpload",
      component: () => import("@/views/components/upload/index.vue"),
      meta: {
        title: $t("menus.pureUpload")
      }
    },
    {
      path: "/components/check-card",
      name: "CheckCard",
      component: () => import("@/views/components/check-card.vue"),
      meta: {
        title: $t("menus.pureCheckCard")
      }
    },
    {
      path: "/components/date-picker",
      name: "DatePicker",
      component: () => import("@/views/components/date-picker.vue"),
      meta: {
        title: $t("menus.pureDatePicker")
      }
    },
    {
      path: "/components/datetime-picker",
      name: "DateTimePicker",
      component: () => import("@/views/components/datetime-picker.vue"),
      meta: {
        title: $t("menus.pureDateTimePicker")
      }
    },
    {
      path: "/components/time-picker",
      name: "TimePicker",
      component: () => import("@/views/components/time-picker.vue"),
      meta: {
        title: $t("menus.pureTimePicker")
      }
    },
    {
      path: "/components/icon-select",
      name: "IconSelect",
      component: () => import("@/views/components/icon-select.vue"),
      meta: {
        title: $t("menus.pureIconSelect")
      }
    },
    {
      path: "/components/animatecss",
      name: "AnimateCss",
      component: () => import("@/views/components/animatecss.vue"),
      meta: {
        title: $t("menus.pureAnimatecss")
      }
    },
    {
      path: "/components/cropping",
      name: "Cropping",
      component: () => import("@/views/components/cropping/index.vue"),
      meta: {
        title: $t("menus.pureCropping")
      }
    },
    {
      path: "/components/segmented",
      name: "Segmented",
      component: () => import("@/views/components/segmented.vue"),
      meta: {
        title: $t("menus.pureSegmented")
      }
    },
    {
      path: "/components/text",
      name: "PureText",
      component: () => import("@/views/components/text.vue"),
      meta: {
        title: $t("menus.pureText")
      }
    },
    {
      path: "/components/slider",
      name: "PureSlider",
      component: () => import("@/views/components/slider/index.vue"),
      meta: {
        title: $t("menus.pureSlider"),
        extraIcon: "IF-pure-iconfont-new svg"
      }
    },
    {
      path: "/components/el-button",
      name: "PureButton",
      component: () => import("@/views/components/el-button.vue"),
      meta: {
        title: $t("menus.pureElButton")
      }
    },
    {
      path: "/components/check-button",
      name: "CheckButton",
      component: () => import("@/views/components/check-button.vue"),
      meta: {
        title: $t("menus.pureCheckButton")
      }
    },
    {
      path: "/components/button",
      name: "ButtonPage",
      component: () => import("@/views/components/button.vue"),
      meta: {
        title: $t("menus.pureButton")
      }
    },
    {
      path: "/components/progress",
      name: "PureProgress",
      component: () => import("@/views/components/progress.vue"),
      meta: {
        title: $t("menus.pureProgress")
      }
    },
    {
      path: "/components/tag",
      name: "PureTag",
      component: () => import("@/views/components/tag.vue"),
      meta: {
        title: $t("menus.pureTag")
      }
    },
    {
      path: "/components/statistic",
      name: "Statistic",
      component: () => import("@/views/components/statistic.vue"),
      meta: {
        title: $t("menus.pureStatistic")
      }
    },
    {
      path: "/components/collapse",
      name: "Collapse",
      component: () => import("@/views/components/collapse.vue"),
      meta: {
        title: $t("menus.pureCollapse")
      }
    },
    {
      path: "/components/cascader",
      name: "Cascader",
      component: () => import("@/views/components/cascader.vue"),
      meta: {
        title: $t("menus.pureCascader")
      }
    },
    {
      path: "/components/color-picker",
      name: "ColorPicker",
      component: () => import("@/views/components/color-picker.vue"),
      meta: {
        title: $t("menus.pureColorPicker")
      }
    },
    {
      path: "/components/selector",
      name: "Selector",
      component: () => import("@/views/components/selector.vue"),
      meta: {
        title: $t("menus.pureSelector")
      }
    },
    {
      path: "/components/waterfall",
      name: "Waterfall",
      component: () => import("@/views/components/waterfall/index.vue"),
      meta: {
        title: $t("menus.pureWaterfall")
      }
    },
    {
      path: "/components/split-pane",
      name: "SplitPane",
      component: () => import("@/views/components/split-pane.vue"),
      meta: {
        title: $t("menus.pureSplitPane")
      }
    },
    {
      path: "/components/swiper",
      name: "Swiper",
      component: () => import("@/views/components/swiper.vue"),
      meta: {
        title: $t("menus.pureSwiper")
      }
    },
    {
      path: "/components/timeline",
      name: "TimeLine",
      component: () => import("@/views/components/timeline.vue"),
      meta: {
        title: $t("menus.pureTimeline")
      }
    },
    {
      path: "/components/count-to",
      name: "CountTo",
      component: () => import("@/views/components/count-to.vue"),
      meta: {
        title: $t("menus.pureCountTo")
      }
    },
    {
      path: "/components/contextmenu",
      name: "ContextMenu",
      component: () => import("@/views/components/contextmenu/index.vue"),
      meta: {
        title: $t("menus.pureContextmenu")
      }
    },
    {
      path: "/components/json-editor",
      name: "JsonEditor",
      component: () => import("@/views/components/json-editor.vue"),
      meta: {
        title: $t("menus.pureJsonEditor")
      }
    },
    {
      path: "/components/seamless-scroll",
      name: "SeamlessScroll",
      component: () => import("@/views/components/seamless-scroll.vue"),
      meta: {
        title: $t("menus.pureSeamless")
      }
    },
    {
      path: "/components/virtual-list",
      name: "VirtualList",
      component: () => import("@/views/components/virtual-list/index.vue"),
      meta: {
        title: $t("menus.pureVirtualList")
      }
    }
  ]
} satisfies RouteConfigsTable;
