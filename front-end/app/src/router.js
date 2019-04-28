import Vue from 'vue'
import Router from 'vue-router'
import addBook from './components/addBook.vue'
import editBook from  './components/editBook.vue'
//import ViewPost from './components/viewBooks.vue'
import ViewPosts from '@/components/viewBooks.vue'


Vue.use(Router);

export default new Router({
    routes: [
        {
            path: '/',
            name: 'home',
            component: ViewPosts
        },
        {
            path: '/mathematics',
            name: 'Mathematics',
            component: ViewPosts
        },
        {
            path: '/physics',
            name: 'Physics',
            component: ViewPosts
        },
        {
            path: '/chemistry',
            name: 'Chemistry',
            component: ViewPosts
        },
        {
            path: '/biology',
            name: 'Biology',
            component: ViewPosts
        },
        {
            path: '/cs',
            name: 'Computer_Science',
            component: ViewPosts
        },
        {
            path: '/varia',
            name: 'Varia',
            component: ViewPosts
        },
        {
            path: '/addbook',
            name: 'addbook',
            component: addBook
        },
        {
            path: '/editbook/:Pid',
            name: 'editbook',
            component: editBook
        },

        {
            path: '/search=:searchTerm',
            name: 'search',
            component: ViewPosts
        },

    ]
})
