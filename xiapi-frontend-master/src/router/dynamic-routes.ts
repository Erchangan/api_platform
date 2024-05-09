import type {RouteRecordRaw} from 'vue-router'

export const ROOT_ROUTE_REDIRECT_PATH = '/dashboard'
const Layout = () => import('~/layouts/index.vue')
const basicRouteMap = {
    // iframe模式下使用
    Iframe: () => import('~/pages/common/iframe.vue'),
    // 一般用于存在子集的页面
    RouteView: () => import('~/pages/common/route-view.vue'),
}
export const rootRoute: RouteRecordRaw = {
    path: '/',
    name: 'rootPath',
    redirect: ROOT_ROUTE_REDIRECT_PATH,
    component: Layout,
    children: [],
}

export default [
    {
        path: '/',
        redirect: '/index',
        name: '/',
        meta: {
            title: '主页',
            icon: 'HomeOutlined',
        },
        component: basicRouteMap.RouteView,
        children: [
            {
                path: '/index',
                name: 'index',
                component: () => import('~/pages/index/index.vue'),
                meta: {
                    title: '接口市场',
                },
            },
        ],
    },
    {
        path: '/analyze',
        redirect: '/analyze/interfaceInfoAnalyze',
        name: 'analyze',
        meta: {
            title: '分析',
            icon: 'LineChartOutlined',
        },
        component: basicRouteMap.RouteView,
        children: [
            {
                path: '/interfaceInfoAnalyze',
                name: 'interfaceInfoAnalyze',
                component: () => import('~/pages/analyze/Analyze.vue'),
                meta: {
                    title: '接口分析',
                },
            },
        ],
    },
    {
        path: '/user',
        redirect: '/user/usercenter',
        name: 'user',
        meta: {
            title: '个人中心',
            icon: 'UserOutlined',
        },
        component: basicRouteMap.RouteView,
        children: [
            {
                path: '/usercenter',
                name: 'usercenter',
                component: () => import('~/pages/user/UserCenter.vue'),
                meta: {
                    title: '个人中心',
                },
            },
            {
                path: '/chatroom',
                name: 'chatroom',
                component: () => import('~/pages/user/ChatRoom.vue'),
                meta: {
                    title: '客服中心',
                },
            },
            {
                path: '/devplatform',
                name: 'devplatform',
                component: () => import('~/pages/user/DevPlatform.vue'),
                meta: {
                    title: '开放平台',
                },
            },
            {
                path: '/orderperson',
                name: 'orderperson',
                component: () => import('~/pages/order/OrderPerson.vue'),
                meta: {
                    title: '个人订单',
                },
            },
        ],
    },
] as RouteRecordRaw[]

export const adminRoute = {
    path: '/admin',
    redirect: '/admin/interfaceInfo',
    name: 'admin',
    meta: {
        title: '管理',
        icon: 'MenuUnfoldOutlined',
    },
    component: basicRouteMap.RouteView,
    children: [
        {
            path: '/interfaceInfo',
            name: 'interfaceInfo',
            component: () => import('~/pages/interfaceinfo/InterfaceInfo.vue'),
            meta: {
                title: '接口管理',
            },
        },
        {
            path: '/interfaceInfoProduct',
            name: 'interfaceInfoProduct',
            component: () => import('~/pages/interfaceinfoproduct/InterfaceInfoProduct.vue'),
            meta: {
                title: '接口商品管理',
            },
        }
    ],
}
