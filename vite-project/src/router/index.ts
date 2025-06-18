// import { createRouter, createWebHashHistory } from "vue-router";
//
// const router = createRouter({
//     history: createWebHashHistory(),
//     routes: [
//         // {
//         //     path: '/',
//         //     redirect: '/home/all-products', // 默认跳转到 /home/all-products
//         // },
//         {
//             path: '/',
//             redirect: '/login', // 主页重定向到登录页
//         },
//         {
//             path: '/login',
//             component: () => import('../views/user/Login.vue'),
//             meta: { title: '用户登录' },
//         },
//         {
//             path: '/register',
//             component: () => import('../views/user/Register.vue'),
//             meta: { title: '用户注册' },
//         },
//         {
//             path: '/home',
//             redirect: '/dashboard', // 访问 home 时重定向到 dashboard
//             component: () => import('../views/Home.vue'),
//             children: [
//                 {
//                     path: 'dashboard', // 子路由路径，去掉前导斜杠
//                     name: 'Dashboard',
//                     component: () => import('../views/user/Dashboard.vue'),
//                     meta: { title: '个人信息' },
//                 },
//                 {
//                     path: 'all-products', // 添加商店列表页面
//                     name: 'AllProducts',
//                     component: () => import('../views/product/AllProduct.vue'), // 确保路径正确
//                     meta: { title: '商品列表' },
//                 },
//                 {
//                     path: 'create-store',
//                     name: 'CreateStore',
//                     component: () => import('../views/product/CreateProduct.vue'), // 确保路径正确
//                     meta: { title: '创建商品' },
//                 },
//                 {
//                     path: 'product/:productId', // 动态路由，用于商店详情
//                     name: 'ProductDetail',
//                     component: () => import('../views/product/ProductDetail.vue'), // 确保路径正确
//                     meta: { title: '商店详情' },
//                     // children: [
//                     //     {
//                     //         path: 'create_product', // 在商店详情下添加创建商品路由
//                     //         name: 'CreateProduct',
//                     //         component: () => import('../views/product/CreateProduct.vue'), // 确保路径正确
//                     //         meta: { title: '创建商品' },
//                     //     },
//                     // ],
//                 },
//             ],
//         },
//         {
//             path: '/404',
//             name: '404',
//             component: () => import('../views/NotFound.vue'),
//             meta: { title: '404' },
//         },
//         {
//             path: '/:catchAll(.*)', // 捕获所有未定义的路径
//             redirect: '/404',
//         },
//     ],
// });
//
// // 路由导航守卫
// router.beforeEach((to, _, next) => {
//     const token: string | null = sessionStorage.getItem('token');
//     const role: string | null = sessionStorage.getItem('role');
//
//     // 设置页面标题
//     if (to.meta.title) {
//         document.title = to.meta.title;
//     }
//
//     // 用户已登录
//     if (token) {
//         if (to.meta.permission) {
//             if (to.meta.permission.includes(role!)) {
//                 next();
//             } else {
//                 next('/404');
//             }
//         } else {
//             next();
//         }
//     } else {
//         // 用户未登录
//         if (to.path === '/login' || to.path === '/register') {
//             next();
//         } else {
//             next('/login');
//         }
//     }
// });
//
// export { router };

import { createRouter, createWebHashHistory } from "vue-router";
//import CartItem from "../views/CartItem.vue";

const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        // {
        //     path: '/',
        //     redirect: '/home/all-products', // 保留这一个重定向规则
        // },
        {
            path: '/login',
            component: () => import('../views/user/Login.vue'),
            meta: { title: '用户登录' },
        },
        {
            path: '/register',
            component: () => import('../views/user/Register.vue'),
            meta: { title: '用户注册' },
        },
        {
            path: '/home',
            redirect: '/dashboard', // 访问 home 时重定向到 dashboard
            component: () => import('../views/Home.vue'),
            children: [
                {
                    path: 'dashboard', // 子路由路径，去掉前导斜杠
                    name: 'Dashboard',
                    component: () => import('../views/user/Dashboard.vue'),
                    meta: { title: '个人信息' },
                },
                {
                    path: 'cart', // 子路由路径，去掉前导斜杠
                    name: 'Cart',
                    component: () => import('../views/cart/CartView.vue'),
                    meta: { title: '购物车' },
                },
                {
                    path: 'order/:orderId/:totalAmount/:realAmount/:createTime',
                    name: 'Order',
                    component: () => import('../views/Order/OrderView.vue'),
                    meta: {title: '订单'},
                },
                {
                    path: 'all-products', // 添加商店列表页面
                    name: 'AllProducts',
                    component: () => import('../views/product/AllProduct.vue'), // 确保路径正确
                    meta: { title: '商品列表' },
                },
                {
                    path: 'all-advertisements', // 子路由路径，去掉前导斜杠
                    name: 'AllAdvertisements',
                    component: () => import('../views/advertisement/AllAdvertisement.vue'),
                    meta: { title: '广告列表' },
                },
                {
                    path: 'all-postings', // 子路由路径，去掉前导斜杠
                    name: 'AllPostings',
                    component: () => import('../views/posting/AllPosting.vue'),
                    meta: { title: '帖子列表' },
                },
                {
                    path: '/home/search/:keyword',
                    name: 'SearchResults',
                    component: () => import('../views/search/SearchResult.vue'),
                    props: true
                },
                {
                    path: 'rankings',
                    name: 'Rankings',
                    component: () => import('../views/ranking/AllRankings.vue')  // 示例
                },
                {
                    path: 'create-product',
                    name: 'CreateProduct',
                    component: () => import('../views/product/CreateProduct.vue'), // 确保路径正确
                    meta: { title: '创建商品' },
                },
                {
                    path: 'create-advertisement',
                    name: 'CreateAdvertisement',
                    component: () => import('../views/advertisement/CreateAdvertisement.vue'), // 确保路径正确
                    meta: { title: '创建广告' },
                },
                {
                    path: 'create-posting',
                    name: 'CreatePosting',
                    component: () => import('../views/posting/CreatePosting.vue'), // 确保路径正确
                    meta: { title: '创建帖子' },
                },
                {
                    path: 'product/:productId/create-comment',
                    name: 'CreateComment',
                    component: () => import('../views/comment/CreateComment.vue'),
                    meta: { title: '撰写评价' },
                },


                {
                    path: 'product/:productId', // 动态路由，用于商品详情
                    name: 'ProductDetail',
                    component: () => import('../views/product/ProductDetail.vue'), // 确保路径正确
                    meta: { title: '商品详情' },
                    // children: [
                    //     {
                    //         path: 'comments',
                    //         name: 'ProductComments',
                    //         component: () => import('../views/comment/TheAllComment.vue'),
                    //         meta: { title: '商品评价' },
                    //         children: [
                    //             {
                    //                 path: ':commentId',
                    //                 name: 'CommentDetail',
                    //                 component: () => import('../views/comment/CommentDetail.vue'),
                    //                 meta: { title: '评价详情' },
                    //             },
                    //
                    //
                    //         ],
                    //     },
                    //
                    // ],
                },
                // {
                //     path: '/cart',
                //     name: 'Cart',
                //     component: CartItem,
                // },
                {
                    path: 'postings/:id',
                    name: 'PostingDetail',
                    component: () => import('../views/posting/PostingDetail.vue'),
                    meta: { title: '帖子详情' },
                },
                {
                    path: 'product/:productId/comments',
                    name: 'ProductComments',
                    component: () => import('../views/comment/TheAllComment.vue'),
                    meta: { title: '商品评价' }
                },
                {
                    path: 'product/:productId/comments/:commentId',
                    name: 'CommentDetail',
                    component: () => import('../views/comment/CommentDetail.vue'),
                    meta: { title: '评价详情' }
                }

            ],
        },
        {
            path: '/404',
            name: '404',
            component: () => import('../views/NotFound.vue'),
            meta: { title: '404' },
        },
        {
            path: '/:catchAll(.*)', // 捕获所有未定义的路径
            redirect: '/404',
        },
    ],
});

// 路由导航守卫
router.beforeEach((to, _, next) => {
    const token: string | null = sessionStorage.getItem('token');
    const role: string | null = sessionStorage.getItem('role');

    // 设置页面标题
    if (to.meta.title) {
        document.title = to.meta.title;
    }

    // 用户已登录
    if (token) {
        if (to.meta.permission) {
            if (to.meta.permission.includes(role!)) {
                next();
            } else {
                next('/404');
            }
        } else {
            next();
        }
    }
    else {
        //用户未登录
        if (to.path === '/login' || to.path === '/register' ) {
            next();
        } else {
            next('/login');
        }
        //next();
    }
});

export { router };