<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" max-width="600">
      <div>
        <v-tabs v-model="tab" centered>
          <v-tabs-slider></v-tabs-slider>

          <v-tab :href="`#data`"> 资料 </v-tab>
          <!-- 个人资料 -->
          <v-tab-item :value="'data'">
            <v-card max-width="600" class="mx-auto">
              <v-img
                src="https://pic1.zhimg.com/v2-4e36b3511ae845536ee47c5b426b58f3_r.jpg?source=1940ef5c"
                height="300px"
                dark
              >
                <v-row class="fill-height">
                  <v-col cols="12">
                    <v-card-title>
                      <v-spacer></v-spacer>
                    </v-card-title>

                    <v-spacer></v-spacer>

                    <v-card-title class="white--text pl-12 pt-12">
                    </v-card-title>
                  </v-col>
                  <v-col cols="12" class="text-center">
                    <v-avatar color="indigo" size="110">
                      <img :src="avatar" :alt="nickname" />
                    </v-avatar>
                  </v-col>
                </v-row>
              </v-img>

              <v-list two-line>
                <v-list-item>
                  <v-list-item-icon>
                    <v-icon color="indigo">mdi-account</v-icon>
                  </v-list-item-icon>

                  <v-list-item-content>
                    <v-list-item-title>{{ username }}</v-list-item-title>
                    <v-list-item-subtitle>用户名</v-list-item-subtitle>
                  </v-list-item-content>

                  <v-list-item-icon> </v-list-item-icon>
                </v-list-item>

                <v-list-item>
                  <v-list-item-action></v-list-item-action>

                  <v-list-item-content>
                    <v-list-item-title>{{ nickname }}</v-list-item-title>
                    <v-list-item-subtitle>昵称</v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item>
                <v-divider inset></v-divider>
                <v-list-item>
                  <v-list-item-icon>
                    <v-icon color="indigo">mdi-email</v-icon>
                  </v-list-item-icon>

                  <v-list-item-content>
                    <v-list-item-title>{{ email }}</v-list-item-title>
                    <v-list-item-subtitle>邮箱</v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item>

                <v-list-item>
                  <v-list-item-action></v-list-item-action>

                  <v-list-item-content>
                    <v-list-item-title>{{ wechat }}</v-list-item-title>
                    <v-list-item-subtitle>微信</v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item>

                <!-- <v-divider inset></v-divider> -->
              </v-list>
            </v-card>
          </v-tab-item>
        </v-tabs>
      </div>
    </v-dialog>
  </v-row>
</template>

<script>
import { blogUserInfo } from "@/api/user";
export default {
  data() {
    return {
      tab: undefined,
    };
  },
  computed: {
    dialog: {
      get() {
        this.handleBlogUserInfo();
        return this.$store.state.user.personProfileDialog;
      },
      set(newValue) {
        this.$store.commit("user/CLOSE_PERSONPROFILEDIALOG", newValue);
      },
    },
    avatar() {
      return this.$store.state.user.userInfo.avatar;
    },
    username() {
      return this.$store.state.user.userInfo.username;
    },
    nickname() {
      return this.$store.state.user.userInfo.nickname;
    },
    email() {
      return this.$store.state.user.userInfo.email;
    },
    wechat() {
      return this.$store.state.user.userInfo.wechat;
    },
  },
  methods: {
    handleBlogUserInfo() {
      const userId = this.$store.state.user.personProfileInfoUserId;
      if (userId !== undefined) {
        blogUserInfo(userId).then((res) => {
          this.$store.state.user.userInfo.username = res.data.username;
          this.$store.state.user.userInfo.nickname = res.data.nickname;
          this.$store.state.user.userInfo.email = res.data.email;
          this.$store.state.user.userInfo.wechat = res.data.wechat;
          this.$store.state.user.userInfo.avatar = res.data.avatar;
        });
      }
    },
  },
};
</script>