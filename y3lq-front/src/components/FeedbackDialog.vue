<template>
  <v-row justify="center">
    <v-dialog v-model="dialogFormVisible" persistent max-width="450">
      <v-card>
        <v-btn text class="mt-2" @click="dialogFormVisible = false">
          <v-icon>mdi-arrow-left</v-icon>
        </v-btn>
        <v-card-title class="headline ml-3">反馈</v-card-title>
        <v-form class="ml-7 mr-7">
          <v-container>
            <v-row>
              <v-col cols="12">
                <v-text-field v-model="nickname" label="尊称"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-text-field v-model="email" label="联系邮箱"></v-text-field>
              </v-col>
              <v-col cols="12">
                <v-textarea label="反馈内容" outlined rows="5" />
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
                  @click="handleSubmit"
                >
                  <strong>提交反馈</strong>
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
export default {
  data() {
    return {
      email: undefined,
      nickname: undefined,
      dialogFormVisible: false,
    };
  },
  methods: {
    handleSubmit(){

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