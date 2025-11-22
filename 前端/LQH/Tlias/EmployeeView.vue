<template>

    <el-container style="height:500px;border:1px solid rgb(230,230,230)">
        <el-header style="font-size:40px">Tlias系统</el-header>
        <el-container>

            <!-- 左侧侧边栏 -->
            <el-aside width="230px" style="background-color: rgb(223,223,223)">

                <el-menu :default-openeds="['1', '3']">
                    <el-sub-menu index="1">
                        <template #title><i class="el-icon-message"></i>系统信息管理</template>
                        <el-menu-item-group>
                            <el-menu-item index="1-1">

                                <router-link to="/DepartmentView">教职工管理</router-link>

                            </el-menu-item>
                            <el-menu-item index="1-2">

                                <router-link to="/EmployeeView">学生管理</router-link>

                            </el-menu-item>
                        </el-menu-item-group>
                    </el-sub-menu>

                </el-menu>

            </el-aside>


            <!-- main主体 -->
            <el-main>
                <el-scrollbar>
                    <!-- 表单 -->
                    <el-form :inline="true" :model="searchForm" class="demo-form-inline">

                        <el-form-item label="Name">
                            <el-input v-model="searchForm.name" placeholder="Name" clearable />
                        </el-form-item>

                        <el-form-item label="Gender">
                            <el-select v-model="searchForm.gender" placeholder="Gender" clearable>
                                <el-option label="男" value="xy" />
                                <el-option label="女" value="xx" />
                                <el-option label="Walmart bug" value="douche bag" />
                                <el-option label="Armor Helicopter" value="Armor Helicopter" />
                                <el-option label="Arom Piercing" val="AP" />
                            </el-select>
                        </el-form-item>

                        <el-form-item label="Hire date">
                            <el-date-picker v-model="searchForm.date" type="daterange" range-separator="To"
                                start-placeholder="Start date" end-placeholder="End date" :size="size" />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="onQuery">Query</el-button>
                        </el-form-item>
                    </el-form>

                    <br>
                    <!-- 学生注册(添加学生) -->
                    <!-- 嵌套在dialog里面的表单 -->
                    <el-button type="primary" @click="openDialog">学生注册</el-button>

                    <el-dialog title="表单" v-model="dialogTableVisible">
                        <!-- dialog -->

                        <el-form ref="form" :model="registerForm" label-width="80px">
                            <!-- form -->
                            <el-form-item label="姓名">
                                <el-input v-model="registerForm.name"></el-input>
                            </el-form-item>

                            <el-form-item label="照片链接">
                                <el-input v-model="registerForm.picture"></el-input>
                            </el-form-item>

                            <el-form-item label="性别">
                                <el-input v-model="registerForm.gender"></el-input>
                            </el-form-item>

                            <el-form-item label="注册时间">
                                <el-col :span="11">
                                    <el-date-picker type="date" placeholder="选择日期" v-model="registerForm.date" value-format="YYYY-MM-DD"
                                        style="width: 100%;"></el-date-picker>
                                </el-col>
                            </el-form-item>

                            <el-form-item label="家庭住址">
                                <el-input v-model="registerForm.address"></el-input>
                            </el-form-item>

                            <el-form-item>
                                <el-button type="primary" @click="onSubmit">立即注册</el-button>
                                <el-button>取消</el-button>
                            </el-form-item>

                        </el-form>

                    </el-dialog>


                    <br>
                    <br>

                    <!-- 表格 -->
                    <el-table :data="tableData" border>
                        <el-table-column prop="name" label="Name" width="120" />
                        <el-table-column prop="picture" label="Picture" width="130">

                            <template #default="scope">
                                <img :src="scope.row.picture" width="70px" height="70px">
                            </template>

                        </el-table-column>
                        <el-table-column prop="gender" label="Gender" width="105" />
                        <el-table-column prop="date" label="Date" width="140" />
                        <el-table-column prop="address" label="Address" />
                    </el-table>

                    <br>

                    <!-- 分页条 -->
                    <el-pagination background layout="sizes, prev ,pager, next,jumper ,total"
                        @size-change="handleSizeChange" @current-change="handleCurrentChange" :total="1000">
                    </el-pagination>

                </el-scrollbar>

            </el-main>

        </el-container>
    </el-container>

</template>


<script>
import axios from 'axios';         //只要vue对象创建，挂载完成，axios会自动发送请求加载数据

export default {
    data() {
        return {
            searchForm: {
                name: '',
                picture: '',
                gender: '',
                date: [],               //假设是开始时间和结束时间可以用数组来存储
                address: ''
            },
            tableData: [
            ],

            registerForm: {
                name: '',
                picture: '',
                gender: '',
                date: [],               //假设是开始时间和结束时间可以用数组来存储
                address: ''
            },
            dialogTableVisible: false
        }
    },
    methods: {
        onQuery() {
            alert("已经提交");
        },
        handleCurrentChange(newPage) {
            alert("现在是第" + newPage + "页");
        },
        handleSizeChange(newSize) {
            alert("现在一页有" + newSize + "条");
        },
        loadData() {
            axios.get("http://192.168.66.122:8080/dataStu").then((result) => {
                this.tableData = result.data.data;
            });
        },
        onSubmit() {
            axios.post("http://192.168.66.122:8080/register", this.registerForm).then(() => {
                alert("注册成功");
                // alert(this.registerForm.date);
                //刷新表
                this.registerForm={};
                this.loadData();
            }).catch(() => {
                alert("注册失败");
            });
        },
        openDialog() {
            this.dialogTableVisible = true;
        }
    },
    mounted() {                                               //钩子方法
        //发送异步请求来获取数据
        // axios.get("/test_data/tableData.json").then((result) => {
        //     this.tableData = result.data.data;
        // });
        //从本地获取

        //从后端服务器获取
        axios.get("http://192.168.66.122:8080/dataStu").then((result) => {
            this.tableData = result.data.data;
        });
        //get里面填的是URL地址
    }
}
</script>



<style></style>