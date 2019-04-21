<template>
    <div id="app">

        <UploadFiles/>

    </div>
</template>

<script>

    import UploadFiles from './components/UploadFile.vue';
    export default {
        name: "app",
        components: {
            UploadFiles
        }

    }
</script>

<style>
    div {
        text-align: center;
    }

    body {
        font-family: Arial, Helvetica, sans-serif;
        text-align: center;
    }

    nav {
        background-color: #D6BDF3 !important;
    }

    li {
        list-style-type: none;
    }

    nav li:hover,
    nav li.router-link-active,
    nav li.router-link-exact-active {
        text-decoration: underline;
        /*text-align: center;
        background-color: #fff;
        cursor: pointer;
        border: 1px solid #333;
        border-radius: 4px;*/
    }

    .error {
        color: red;
        font-weight: bolder;
    }

</style>
