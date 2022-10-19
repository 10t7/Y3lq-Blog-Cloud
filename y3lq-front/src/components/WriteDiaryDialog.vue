<template>
  <v-row justify="center">
    <v-dialog v-model="dialogFormVisible" persistent max-width="450">
      <v-card>
        <v-btn text class="mt-2" @click="dialogFormVisible = false">
          <v-icon>mdi-arrow-left</v-icon>
        </v-btn>
        <v-card-title class="headline ml-3">编写日记</v-card-title>
        <v-form class="ml-7 mr-7">
          <v-container>
            <v-row>
              <!-- <v-col cols="12">
                <v-text-field v-model="nickname" label="尊称"></v-text-field>
              </v-col> -->
              <!-- <v-col cols="12">
                <v-text-field v-model="email" label="联系邮箱"></v-text-field>
              </v-col> -->
              <v-col cols="12">
                <v-textarea
                  v-model="content"
                  label="这一刻的想法..."
                  rows="7"
                />
              </v-col>
            </v-row>
            <v-row align="center" class="mb-3">
              <v-col class="text-center" cols="12"
                ><v-btn
                  class="text-center"
                  color="primary"
                  block
                  large
                  rounded
                  @click="handleSubmit()"
                >
                  <strong>发表</strong>
                  <v-icon>mdi-swap-horizontal</v-icon></v-btn
                ></v-col
              >
            </v-row>
          </v-container>
        </v-form>
      </v-card>
    </v-dialog>
  </v-row>
</template>

<script>
import { addDiary } from "@/api/diary";
export default {
  data() {
    return {
      content: undefined,
      dialogFormVisible: false,
    };
  },
  mounted() {
    this.content = undefined;
  },
  methods: {
    handleSubmit() {
      const data = {
        content: this.content,
      };
      if(this.content == undefined|| this.content==""){
        this.$store.dispatch("snackbar/openSnackbar", {
          msg: "请输入内容",
          color: "error",
        });
        return
      }
      addDiary(data).then((res) => {
        this.content = undefined;
        this.$store.dispatch("snackbar/openSnackbar", {
          msg: "发表成功",
          color: "success",
        });
        this.handleClose()
      });
    },
    openDialog() {
      this.dialogFormVisible = true;
    },
    handleClose() {
      this.dialogFormVisible = false;
    },
  },
};
</script>