<template>
    <el-container style="height:500px;border:1px solid rgb(230,230,230)">
        <el-header style="font-size:40px">Tlias系统</el-header>
        <el-container>
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

            <el-main>


                <el-scrollbar>
                    <!-- 表单 -->
                    <el-form :inline="true" :model="tableForm" class="demo-form-inline">

                        <el-form-item label="Name">
                            <el-input v-model="tableForm.name" placeholder="Name" clearable />
                        </el-form-item>

                        <el-form-item label="Gender">
                            <el-select v-model="tableForm.gender" placeholder="Gender" clearable>
                                <el-option label="男" value="xy" />
                                <el-option label="女" value="xx" />
                                <el-option label="Walmart bug" value="douche bag" />
                                <el-option label="Armor Helicopter" value="Armor Helicopter" />
                                <el-option label="Arom Piercing" val="AP" />
                            </el-select>
                        </el-form-item>

                        <el-form-item label="Hire date">
                            <el-date-picker v-model="tableForm.date" type="daterange" range-separator="To"
                                start-placeholder="Start date" end-placeholder="End date" :size="size" />
                        </el-form-item>
                        <el-form-item>
                            <el-button type="primary" @click="onSubmit">Query</el-button>
                        </el-form-item>
                    </el-form>

                    <br>

                    <!-- 表格 -->
                    <el-table :data="tableData" border>
                        <el-table-column prop="name" label="Name" width="120" />
                        <el-table-column prop="position" label="Postion" width="130" />
                        <el-table-column pro="department" label="Department" width="130" />
                        <el-table-column prop="gender" label="Gender" width="105" />
                        <el-table-column prop="date" label="Date" width="140" />
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
import axios from 'axios';

export default {

    data() {
        return {
            tableForm: {
                name: '',
                position: '',
                department: '',
                age: '',
                gender: '',
                date: []               //假设是开始时间和结束时间可以用数组来存储
            },
            tableData: [
            ]
        }
    },
    methods: {
        onSubmit() {
            alert("已经提交");
        },
        handleCurrentChange(newPage) {
            alert("现在是第" + newPage + "页");
        },
        handleSizeChange(newSize) {
            alert("现在一页有" + newSize + "条");
        }
    },
    mounted() {
        axios.get("http://192.168.66.122:8080/dataFacul").then((result) => {
            this.tableData=result.data.data;
        })
    }
}

</script>

<style></style>