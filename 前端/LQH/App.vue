<template>                <!-- template部分负责生成html代码 -->

  <!-- <h1>{{message}}</h1>      通过插值表达式来展示数据模型中的数据 -->
  <!-- <h1>{{comment}}</h1> -->
   <!-- <EmployeeView></EmployeeView> -->
    <router-view></router-view>
  
</template>


<script>

export default{

  components:{},
  data:function(){
    return{

    }          //定义数据模型
  },
  methods:{

  }
}
</script>




<style>



/* #app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
} */
</style>

<!-- Vue的组件文件以.vue为结尾，每个组件由三个部分组成:<template>,<script>,<style> -->

<!-- 据说主要操作的文件就是.vue的文件 -->
 <!-- App.vue是根组件 -->
