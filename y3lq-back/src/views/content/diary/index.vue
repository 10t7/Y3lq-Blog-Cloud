<template>
  <el-main>
    <!-- 搜索按钮 -->
    <el-form :inline="true" class="demo-form-inline" size="mini">
      <el-form-item label="作者">
        <el-input
          placeholder="请输入日记作者用户名"
          v-model="diaryQueryParam.username"
        ></el-input>
      </el-form-item>
      <!-- <el-form-item label="日记状态">
        <el-switch
          v-model="diaryQueryParam.status"
          active-color="#13ce66"
          inactive-color="#ff4949"
        >
        </el-switch>
      </el-form-item> -->
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          @click="handleSearchDiary"
          v-if="hasPermission('content:diary:query')"
          >查 询</el-button
        >
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-refresh-right" @click="handleResetSearchParam"
          >重 置</el-button
        >
      </el-form-item>

      <el-form-item>
        <el-button
          type="danger"
          icon="el-icon-delete"
          @click="handleDeleteDiary()"
          v-if="hasPermission('content:diary:delete')"
          >删 除</el-button
        >
      </el-form-item>
    </el-form>
    <!-- 日记表格 -->
    <el-table
      :data="diaryData"
      stripe
      style="width: 100%"
      @selection-change="handleSelectionChange"
    >
      <!-- 复选框 -->
      <el-table-column
        type="selection"
        width="55"
        align="center"
        fixed
      ></el-table-column>
      <el-table-column
        prop="diaryUsername"
        label="作者用户名"
        width="150"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="diaryUserNickname"
        label="作者昵称"
        width="150"
        align="center"
      >
      </el-table-column>
      <el-table-column
        prop="status"
        label="日记状态"
        width="150"
        align="center"
      >
        <template slot-scope="{ row }">
          <el-switch
            v-model="row.status"
            active-value="0"
            inactive-value="1"
            @change="changeDiaryStatus(row)"
          >
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column
        prop="orderNum"
        label="排序(首页推荐)"
        width="250"
        align="center"
      >
        <template slot-scope="{ row }">
          <el-input-number
            size="small"
            v-model="row.orderNum"
            controls-position="right"
            @change="handleOrderNumChange(row.diaryId, row.orderNum)"
            :min="0"
            :max="50"
          ></el-input-number>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        label="创建时间"
        width="180"
        align="center"
        :formatter="dateFormat"
      >
      </el-table-column>
      <el-table-column
        prop="operation"
        label="操作"
        width="300"
        align="center"
        fixed="right"
      >
        <template slot-scope="{ row }">
          <el-button
            type="warning"
            icon="el-icon-view"
            plain
            size="mini"
            @click="handleViewContent(row)"
            v-if="hasPermission('content:diary:query')"
            >查看</el-button
          >
          <el-button
            type="danger"
            icon="el-icon-delete"
            plain
            size="mini"
            @click="handleDeleteDiary(row.diaryId)"
            v-if="hasPermission('content:diary:delete')"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <el-pagination
      style="margin-top: 15px; textalign: center"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pageNum"
      :page-sizes="[5, 10, 20]"
      :page-size="pageSize"
      layout="sizes, total, prev, pager, next, jumper"
      :total="diaryListTotal"
    >
    </el-pagination>
  </el-main>
</template>

<script>
import moment from "moment";
import {
  getDiaryList,
  changeStatus,
  changeDiaryOrderNum,
  deleteDiary,
} from "@/api/diary";
export default {
  name: "Diary",

  data() {
    return {
      diaryData: [],
      pageNum: 1,
      pageSize: 5,
      diaryListTotal: 0,
      diaryQueryParam: {
        username: undefined,
        status: true,
      },
      selectDiaryIds: [],
    };
  },

  mounted() {
    this.handleDiaryList();
  },

  methods: {
    // 复选框改变保存对应的diaryId
    handleSelectionChange(selection) {
      this.selectDiaryIds = selection.map((item) => item.diaryId);
    },
    // 删除日记
    handleDeleteDiary(diaryId) {
      const diarys = diaryId || this.selectDiaryIds;
      if(diarys == undefined || diarys.length ==0){
          return
      }
      this.$confirm("确定要删除吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          deleteDiary(diarys).then((res) => {
            if (res.code == 200) {
              this.$message({
                message: "删除成功",
                type: "success",
              });
              this.handleDiaryList();
            } else {
              this.$message.error(res.msg);
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    // 负责改变排序
    handleOrderNumChange(diaryId, orderNum) {
      changeDiaryOrderNum(diaryId, orderNum);
    },
    // 负责查看内容
    handleViewContent(row) {
      this.$alert(row.content, "日记内容", {
        confirmButtonText: "关闭",
      });
    },
    // 改变日记状态
    changeDiaryStatus(row) {
      const status = row.status;
      const diaryId = row.diaryId;
      changeStatus(diaryId, status).then((res) => {
        
      });
    },
    // 时间格式化
    dateFormat: function (row, column) {
      var date = row[column.property];
      if (date === undefined) {
        return "";
      }
      return moment(date).format("YYYY-MM-DD HH:mm:ss");
    },

    // 处理重置查询参数
    handleResetSearchParam() {
        this.diaryQueryParam = {
            username:undefined,
            status:true
        }
        this.handleDiaryList()
    },
    // 处理查询日记
    handleSearchDiary() {
      this.handleDiaryList();
    },
    // pageSize改变触发
    handleSizeChange(pageSize) {
      this.pageSize = pageSize;
      this.handleDiaryList();
    },
    // 当前页码改变触发
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum;
      this.handleDiaryList();
    },
    // 获取日记列表
    handleDiaryList() {
      const status = this.diaryQueryParam.status == true ? "0" : "1";
      const username = this.diaryQueryParam.username;
      getDiaryList(this.pageNum, this.pageSize, username, status).then(
        (res) => {
          this.diaryData = res.data.list;
          this.diaryListTotal = res.data.total;
        }
      );
    },
  },
};
</script>

<style lang="scss" scoped>
</style>