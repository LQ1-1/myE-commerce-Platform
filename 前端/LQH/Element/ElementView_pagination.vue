<template>
    
    <!-- 分页组件 -->
    <el-pagination background layout="sizes, prev ,pager, next,jumper ,total" 
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    :total="1000">
    </el-pagination>
    <!-- sizes一次展多少个,prev是否展示往上一页移动的按钮,next是否展示往下一页移动的按钮,jumper跳到多少页,total总共有多少页 -->

</template>



<script>
export default{
    methods:{
        handleSizeChange(newSize){
            alert("每页记录数 : "+newSize),             //类似QT的Qmessagebox
            console.log();
        },
        handleCurrentChange(newPosi){
            alert("页码数 : "+newPosi),
            console.log();
        }
    },
    data(){
        return{

        }
    }
}
</script>



<style></style>