<template>
  <div class="container">
    <div class="filter-container">
      <el-input @keyup.enter.native="handleSearch" style="width: 200px;" class="filter-item" placeholder="任务名称" v-model="listQuery.jobName">
      </el-input>

      <el-input clearable style="width: 200px" class="filter-item" v-model="listQuery.jobGroup" placeholder="任务组">
      </el-input>

      <el-select clearable class="filter-item" style="width: 130px" v-model="listQuery.jobStatus" placeholder="状态">
        <el-option v-for="item in  jobStatusOptions" :key="item.key" :label="item.display_name" :value="item.key">
        </el-option>
      </el-select>

      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
      <el-button class="filter-item" style="margin-left: 10px;" @click="handleCreate" type="primary" icon="el-icon-edit">新增</el-button>
    </div>

    <el-table
      ref="multipleTable"
      :data="tableData"
      align="center"
      header-align="center"
      tooltip-effect="dark"
      style="width: 100%"
      v-loading="listLoading" element-loading-text="加载中。。。"
      @selection-change="handleSelectionChange">

      <el-table-column type="selection" width="40"></el-table-column>
      <el-table-column prop="jobName" label="任务名称" width="120"></el-table-column>
      <el-table-column prop="jobGroup" label="任务组" width="120"></el-table-column>
      <el-table-column prop="springId" label="SpringId" width="120"></el-table-column>
      <el-table-column prop="cron" label="cron" width="120"></el-table-column>
      <el-table-column prop="methodName" label="执行方法" width="120"></el-table-column>
      <el-table-column prop="jobStatus" label="状态" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.jobStatus | jobStatusFilter">{{scope.row.jobStatus | jobStatusNameFilter}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="description" label="任务描述" width="180"></el-table-column>

      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" icon="el-icon-edit" @click="handleEdit(scope.row.id,scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" icon="el-icon-delete" @click="handleDelete(scope.row.id,scope.row)">删除</el-button>
          <el-button size="mini" type="warning" icon="el-icon-close" @click="handlePause(scope.row.id,scope.row)">暂定</el-button>
          <el-button size="mini" type="success" icon="el-icon-check" @click="handleExecute(scope.row.id,scope.row)">立即执行</el-button>
          <el-button size="mini" type="primary" icon="el-icon-check" @click="handleResume(scope.row.id,scope.row)">恢复</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div v-show="!listLoading" class="pagination-container">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page.sync="listQuery.page"
        :page-sizes="[15,25,35,45]" :page-size="listQuery.limit" layout="total, sizes, prev, pager, next, jumper" :total="total" background>
      </el-pagination>
    </div>
    <el-dialog :title="titleDialog" :visible.sync="showDialogVisible" width="50%" center>
      <el-form class="small-space" :model="jobModel" ref="jobModel" label-position="left" label-width="100px" style="width:400px;margin-left:50px;">
        <el-form-item label="SpringId：">
          <el-input v-model="jobModel.springId" :disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item label="任务名：">
          <el-input v-model="jobModel.jobName" :disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item label="任务组：">
          <el-input v-model="jobModel.jobGroup" :disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item label="执行方法：">
          <el-input v-model="jobModel.methodName" :disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item label="执行类名：">
          <el-input v-model="jobModel.className" :disabled="disabled"></el-input>
        </el-form-item>
        <el-form-item label="Cron表达式：">
          <el-input v-model="jobModel.cron"></el-input>
        </el-form-item>
        <el-form-item label="任务描述：">
          <el-input type="textarea" :rows="4" v-model="jobModel.description" :disabled="disabled"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleBack">取 消</el-button>
        <el-button v-if="dialogStatus=='create'" type="success" @click="handleInstance('jobModel')">确 定</el-button>
        <el-button v-else type="primary" @click="handleInstance">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { getList, saveJob, updateJob, deleteJob, executeJob, pauseJob, resumeJob } from 'api/job.js'
  export default {
    name: 'Job',
    data () {
      return {
        tableData: [],
        multipleSelection: [],
        jobStatusOptions: [
          {'key': '0', 'display_name': '暂停'},
          {'key': '1', 'display_name': '删除'},
          {'key': '2', 'display_name': '正常'}],
        importanceOptions: [],
        dialogStatus: 'create',
        showDialogVisible: false,
        show: false,
        total: null,
        titleDialog: '',
        listLoading: true,
        disabled: false,
        jobModel: {},
        listQuery: {
          page: 1,
          limit: 15,
          jobName: undefined,
          jobGroup: undefined,
          jobStatus: undefined,
          sort: '+id'
        }
      }
    },
    filters: {
      jobStatusFilter (status) {
        const statusMap = {
          '0': 'warning',
          '1': 'danger',
          '2': 'success'
        }
        return statusMap[status]
      },
      jobStatusNameFilter (status) {
        if (status === '0') {
          return '暂停'
        } else if (status === '1') {
          return '删除'
        } else if (status === '2') {
          return '正常'
        }
      }
    },
    created () {
      this.loadList()
    },
    methods: {
      loadList () {
        this.listLoading = true
        getList(this.listQuery).then(response => {
          this.tableData = response.record
          this.total = response.total
          this.listLoading = false
          this.showDialogVisible = false
        }).catch(error => {
          console.log(error)
          this.listLoading = false
          this.showDialogVisible = false
        })
      },
      toggleSelection (rows) {
        if (rows) {
          rows.forEach(row => {
            this.$refs.multipleTable.toggleRowSelection(row)
          })
        } else {
          this.$refs.multipleTable.clearSelection()
        }
      },
      handleSelectionChange (val) {
        this.multipleSelection = val
      },
      handleEdit (index, row) {
        this.jobModel = row
        this.showDialogVisible = true
        this.disabled = true
        this.dialogStatus = 'edit'
        this.titleDialog = '编辑'
      },
      handleDelete (index, row) {
        this.$confirm('您确认要删除此Job任务【' + row.jobName + '】任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          this.jobModel = row
          deleteJob({'id': this.jobModel.id}).then(response => {
            if (response.code === 200) {
              this.$message({
                type: 'success',
                message: '删除成功!'
              })
              this.loadList()
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
      },
      handleCreate () {
        this.titleDialog = '创建'
        this.dialogStatus = 'create'
        this.showDialogVisible = true
        this.disabled = false
        this.jobModel = {}
      },
      handleSearch () {
        this.listQuery.page = 1
        this.loadList()
      },
      handlePause (index, row) {
        this.$confirm('您确认要暂停此Job任务【' + row.jobName + '】任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          this.jobModel = row
          pauseJob({'id': this.jobModel.id}).then(response => {
            if (response.code === 200) {
              this.$message({
                type: 'success',
                message: '暂停成功!'
              })
              this.loadList()
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消暂停·'
          })
        })
      },
      handleResume (index, row) {
        this.$confirm('您确认要恢复此Job任务【' + row.jobName + '】任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          this.jobModel = row
          resumeJob({'id': this.jobModel.id}).then(response => {
            if (response.code === 200) {
              this.$message({
                type: 'success',
                message: '恢复成功!'
              })
              this.loadList()
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作·'
          })
        })
      },
      handleExecute (index, row) {
        this.$confirm('您确认要立即执行此Job任务【' + row.jobName + '】任务吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          this.jobModel = row
          executeJob({'id': this.jobModel.id}).then(response => {
            if (response.code === 200) {
              this.$message({
                type: 'success',
                message: '执行成功!'
              })
              this.loadList()
            }
          })
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作·'
          })
        })
      },
      handleSizeChange (val) {
        this.listQuery.limit = val
        this.loadList()
      },
      handleCurrentChange (val) {
        this.listQuery.page = val
        this.loadList()
      },
      handleInstance () {
        if (this.dialogStatus === 'create') {
          saveJob(this.jobModel)
          this.loadList()
        } else {
          updateJob(this.jobModel)
          this.loadList()
        }
      },
      handleBack () {
        this.showDialogVisible = false
        this.loadList()
      }
    }
  }
</script>
<style scope>
  .container {
    padding: 20px;
  }
  .filter-container {
    padding-bottom: 10px;
  }
  .filter-container .filter-item {
    display: inline-block;
    vertical-align: middle;
    margin-bottom: 10px;
  }
</style>
