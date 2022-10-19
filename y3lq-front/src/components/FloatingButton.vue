<template>
  <v-card id="create">
    <v-speed-dial
      v-model="fab"
      :top="top"
      :bottom="bottom"
      :right="right"
      :left="left"
      :direction="direction"
      :open-on-hover="hover"
      :transition="transition"
    >
      <template v-slot:activator>
        <v-btn v-model="fab" color="grey darken-3" dark fab>
          <v-badge :value="dotVisible(fab)" color="red" dot dark>
            <v-icon v-if="fab">mdi-close</v-icon>
            <v-icon v-else>mdi-home</v-icon>
          </v-badge>
        </v-btn>
      </template>
      <v-btn fab dark small color="green" @click="handlePersonalProfile">
        <v-badge
          color="red"
          :content="this.$store.state.user.newNum"
          dark
          :value="this.$store.state.user.newNumVisible"
        >
          <v-icon>mdi-account-circle </v-icon>
        </v-badge>
      </v-btn>
      <v-btn
        v-if="this.$store.state.user.token"
        fab
        dark
        small
        color="indigo"
        @click="handleWriteDiary"
      >
        <v-icon>mdi-pencil-outline</v-icon>
      </v-btn>
      <v-btn
        fab
        dark
        small
        color="#37474F"
        @click="handleWrite"
        v-if="this.$store.state.user.token"
      >
        <v-icon>mdi-pencil</v-icon>
      </v-btn>
      <v-btn
        fab
        dark
        small
        color="red"
        @click="handleLogout"
        v-if="this.$store.state.user.token"
      >
        <v-icon>mdi-logout</v-icon>
      </v-btn>
    </v-speed-dial>

    <!-- 登录注册对话框 -->
    <v-row justify="center">
      <v-dialog
        v-model="dialog"
        persistent
        max-width="450"
        style="z-index: 999"
      >
        <!-- <template v-slot:activator="{ on, attrs }">
          <v-btn color="primary" dark v-bind="attrs" v-on="on">
            Open Dialog
          </v-btn>
        </template> -->
        <v-card>
          <v-btn text class="mt-2" @click="dialog = false">
            <v-icon>mdi-arrow-left</v-icon>
          </v-btn>
          <v-row>
            <v-col cols="20">
              <v-card-title class="headline ml-6">{{ title }}</v-card-title>
            </v-col>
            <v-col cols="4">
              <v-btn
                v-if="type === '0'"
                text
                class="mt-2"
                @click="handleBtn(type, title, btnTitle)"
              >
                注册
              </v-btn>
              <v-btn
                v-if="type === '1'"
                text
                class="mt-2"
                @click="handleBtn(type, title, btnTitle)"
              >
                登录
              </v-btn>
            </v-col>
          </v-row>
          <v-form class="ml-7 mr-7" ref="loginRegisterForm" v-model="valid">
            <v-container>
              <v-row>
                <v-col cols="12" v-if="type === '1'" class="text-center">
                  <v-avatar color="indigo" size="80">
                    <v-icon dark>mdi-account-circle</v-icon>
                  </v-avatar>
                </v-col>
                <v-col cols="12" v-if="type === '1'">
                  <v-text-field
                    :rules="rules.emailRules"
                    v-model="userInfo.email"
                    label="邮箱*"
                    required
                  ></v-text-field>
                </v-col>
                <v-col cols="12" v-if="type === '0'">
                  <v-text-field
                    :rules="rules.usernameRules"
                    v-model="userInfo.username"
                    label="用户名*"
                    required
                  ></v-text-field>
                </v-col>
                <v-col cols="12">
                  <v-text-field
                    :rules="rules.passwordRules"
                    v-model="userInfo.password"
                    type="password"
                    label="密码*"
                    required
                  ></v-text-field>
                </v-col>
                <v-col cols="12" v-if="type === '1'">
                  <v-text-field
                    v-model="userInfo.nickname"
                    label="昵称"
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-row align="center">
                <v-col class="text-center mb-5" cols="12"
                  ><v-btn
                    class="text-center"
                    color="primary"
                    block
                    large
                    rounded
                    :loading="btnLoading"
                    @click="handleSubmit"
                  >
                    <strong>{{ btnTitle }}</strong>
                    <v-icon>mdi-swap-horizontal</v-icon></v-btn
                  ></v-col
                >
              </v-row>
            </v-container>
          </v-form>
          <!-- <small>*indicates required field</small> -->
          <!-- </v-card-text> -->
          <!-- <v-card-actions>
            <v-spacer></v-spacer>
          </v-card-actions> -->
          <!-- <v-row> -->
          <!-- <v-col><v-card-text class="mt-3"> 找回密码 </v-card-text></v-col> -->
          <!-- <v-col cols="21"> -->
          <!-- <v-card-text class="mt-3 mb-10" style="text-align: center">
            注册&emsp;|&emsp;忘记密码？
          </v-card-text> -->
          <!-- <v-card-text class="mt-3" style="text-align: center">
            其他方式登录
          </v-card-text> -->
          <!-- </v-col> -->
          <!-- <v-col cols="3"
              ><v-card-text class="mt-3"> 找回密码 </v-card-text></v-col
            > -->
          <!-- </v-row> -->

          <!-- <v-card-text style="text-align: center">
            <v-icon>mdi-qqchat</v-icon><v-icon class="ml-3">mdi-email</v-icon>
          </v-card-text> -->
        </v-card>
      </v-dialog>
    </v-row>

    <WriteDiaryDialog ref="writeDiaryDialog" />
    <!-- <Profile ref="profileDialog" /> -->
  </v-card>
</template>

<script>
import WriteDiaryDialog from "@/components/WriteDiaryDialog";

import Profile from "@/components/Profile";
import { register } from "@/api/user";

export default {
  components: { WriteDiaryDialog, Profile },
  data: () => ({
    // profileDialogVisible:false,
    // feedbackDialogVisible: false,
    valid: true,
    btnLoading: false,
    rules: {
      emailRules: [
        (v) => !!v || "邮箱有助于找回密码哦",
        (v) => /.+@.+/.test(v) || "邮箱格式不合法",
      ],
      usernameRules: [
        (v) => !!v || "用户名不能为空",
        (v) =>
          (v && v.length >= 2 && v.length <= 18) || "用户名长度应在[2, 18]",
      ],
      passwordRules: [
        (v) => !!v || "密码不能为空",
        (v) => (v && v.length >= 2 && v.length <= 18) || "密码长度应在[2, 18]",
      ],
      nicknameRules: [
        (v) => !!v || "昵称不能为空",
        (v) => (v && v.length >= 2 && v.length <= 18) || "昵称长度应在[2, 18]",
      ],
    },
    userInfo: {
    },
    btnTitle: "继续",
    title: "请先登录",
    // 0:登录  1:注册
    type: "0",
    // dialog: false,
    direction: "top",
    fab: false,
    fling: false,
    hover: false,
    tabs: null,
    top: false,
    right: true,
    bottom: true,
    left: false,
    transition: "slide-y-reverse-transition",
  }),

  methods: {
    handleWrite() {
      this.$store.state.app.writeDialog = true;
    },
    // 红点是否可见
    dotVisible(fab) {
      if (!fab && this.$store.state.user.newNumVisible) {
        return true;
      } else {
        return false;
      }
    },
    // 处理退出
    handleLogout() {
      this.$store.dispatch("user/logout");
      this.$store.dispatch("snackbar/openSnackbar", {
        msg: "退出登录成功",
        color: "success",
      });
    },
    // 处理个人信息
    // handleProfile() {
    //   this.$nextTick(() => {
    //     this.$refs.profileDialog.openDialog();
    //   });
    // },

    // 处理写日记
    handleWriteDiary() {
      this.$nextTick(() => {
        this.$refs.writeDiaryDialog.openDialog();
      });
    },
    // 处理反馈
    handleFeedback() {
      // this.feedbackDialogVisible = true;
      this.$nextTick(() => {
        this.$refs.feedbackDialog.openDialog();
      });
    },
    // 提交登录或者注册
    handleSubmit() {
      if (this.type == "0") {
        // 为用户登录
        this.$refs.loginRegisterForm.validate();
        if (this.valid) {
          this.btnLoading = true;
          this.$store
            .dispatch("user/login", this.userInfo)
            .then(() => {
              this.btnLoading = false;
              this.dialog = false;
              this.$store.dispatch("user/getInfo").then(() => {});
            })
            .catch(() => {
              this.btnLoading = false;
            });
        }
      }
      if (this.type == "1") {
        // 用户注册
        register(this.userInfo).then((res) => {
          this.$store.dispatch("snackbar/openSnackbar", {
            msg: "注册成功",
            color: "success",
          });
          this.handleBtn();
        });
      }
    },
    // 处理切换注册登录按钮
    handleBtn(type, title, btnTitle) {
      this.resetInfo();
      this.type = type === "0" ? "1" : "0";
      this.title = title === "请先登录" ? "用户注册" : "请先登录";
      this.btnTitle = btnTitle === "继续" ? "注册" : "继续";
    },
    // 个人信息
    handlePersonalProfile() {
      if (!this.$store.state.user.token) {
        this.type = "0";
        this.title = "请先登录";
        this.btnTitle = "继续";
        this.resetInfo();
        this.dialog = true;
        return;
      }
      this.$store
        .dispatch("user/getInfo")
        .then(() => {
          this.$store.dispatch("profile/open");
        })
        .catch(() => {
          this.type = "0";
          this.title = "请先登录";
          this.btnTitle = "继续";
          this.resetInfo();
          this.dialog = true;
        });
    },
    resetInfo() {
      if (this.$refs.loginRegisterForm !== undefined) {
        this.$refs.loginRegisterForm.resetValidation();
      }
      this.userInfo = {
        username: undefined,
        password: undefined,
        nickname: undefined,
        email: undefined,
      };
    },
  },

  computed: {
    dialog: {
      get() {
        return this.$store.state.user.loginDialog;
      },
      set(newValue) {
        this.$store.commit("user/CLOSE_LOGINDIALOG", newValue);
      },
    },
    activeFab() {
      switch (this.tabs) {
        case "one":
          return { class: "purple", icon: "account_circle" };
        case "two":
          return { class: "red", icon: "edit" };
        case "three":
          return { class: "green", icon: "keyboard_arrow_up" };
        default:
          return {};
      }
    },
  },

  watch: {
    top(val) {
      this.bottom = !val;
    },
    right(val) {
      this.left = !val;
    },
    bottom(val) {
      this.top = !val;
    },
    left(val) {
      this.right = !val;
    },
  },
};
</script>

<style>
/* This is for documentation purposes and will not be needed in your application */
#create .v-speed-dial {
  position: fixed;
}

#create .v-btn--floating {
  position: relative;
}
</style>