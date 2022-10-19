<template>
  <v-row justify="center">
    <v-dialog v-model="dialog" max-width="600">
      <div>
        <v-tabs v-model="tab" :grow="grow">
          <v-tabs-slider></v-tabs-slider>

          <!-- <v-tab v-for="i in tabs" :key="i" :href="`#tab-${i}`">
            Tab {{ i }}
          </v-tab> -->
          <v-tab :href="`#data`"> 资料 </v-tab>
          <v-tab :href="`#notice`" @click="handleReaded">
            <v-badge
              color="red"
              :content="this.$store.state.user.newNum"
              dark
              :value="this.$store.state.user.newNumVisible"
            >
              动态
            </v-badge>
          </v-tab>
          <!-- <v-tab :href="`#setting`"> 设置 </v-tab> -->

          <!-- 个人资料 -->
          <v-tab-item :value="'data'">
            <v-card max-width="600" class="mx-auto">
              <v-img
                src="https://pic1.zhimg.com/v2-4e36b3511ae845536ee47c5b426b58f3_r.jpg?source=1940ef5c"
                height="300px"
                dark
              >
                <!-- <v-row class="fill-height">
                  <v-card-title> </v-card-title>
                  <v-spacer></v-spacer>
                </v-row> -->
                <v-row class="fill-height">
                  <v-col cols="12">
                    <v-card-title>
                      <!-- <v-btn dark icon>
                        <v-icon>mdi-chevron-left</v-icon>
                      </v-btn> -->

                      <v-spacer></v-spacer>

                      <v-btn dark icon class="mr-4" @click="handleEditProfile">
                        <v-icon>mdi-pencil</v-icon>
                      </v-btn>

                      <!-- <v-btn dark icon>
                      <v-icon>mdi-dots-vertical</v-icon>
                    </v-btn> -->
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

                  <v-list-item-icon>
                    <!-- <v-icon class="mr-5">mdi-pencil</v-icon> -->
                    <!-- <v-icon>mdi-message-text</v-icon> -->
                  </v-list-item-icon>
                </v-list-item>

                <v-list-item>
                  <v-list-item-action></v-list-item-action>

                  <v-list-item-content>
                    <v-list-item-title>{{ nickname }}</v-list-item-title>
                    <v-list-item-subtitle>昵称</v-list-item-subtitle>
                  </v-list-item-content>

                  <!-- <v-list-item-icon>
                    <v-icon>mdi-message-text</v-icon>
                  </v-list-item-icon> -->
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
                <!-- 
                <v-divider inset></v-divider>

                <v-list-item>
                  <v-list-item-icon>
                    <v-icon color="indigo">mdi-web</v-icon>
                  </v-list-item-icon>

                  <v-list-item-content>
                    <v-list-item-title>{{ website }}</v-list-item-title>
                    <v-list-item-subtitle>个人站点</v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item> -->
              </v-list>
            </v-card>
            <!-- <v-card flat tile>
              <v-form class="ml-7 mr-7">
                <v-row>
                  <v-col cols="12" class="text-center mt-4" md="12">
                    <v-avatar color="indigo" size="75">
                      <v-icon dark>mdi-account-circle</v-icon>
                    </v-avatar>
                  </v-col>
                  <v-col cols="12" md="6" class="text-center">
                    <v-chip class="ma-2" color="#3949AB"  text-color="white">
                      订阅 10
                      <v-icon right>mdi-account-star</v-icon>
                    </v-chip>
                  </v-col>
                  <v-col cols="12" md="6" class="text-center">
                    <v-chip class="ma-2" color="#3949AB" text-color="white">
                      粉丝 10
                      <v-icon right>mdi-account-heart</v-icon>
                    </v-chip>
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field label="昵称" single-line solo></v-text-field>
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                      label="用户名"
                      single-line
                      solo
                    ></v-text-field>
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field label="微信" single-line solo></v-text-field>
                  </v-col>
                  <v-col cols="12" md="6">
                    <v-text-field
                      label="个人网站"
                      single-line
                      solo
                    ></v-text-field>
                  </v-col>

                  <v-col cols="12" md="12">
                    <v-textarea
                      label="个人介绍"
                      auto-grow
                      counter
                      rows="4"
                      row-height="33"
                      solo
                    ></v-textarea>
                  </v-col>
                </v-row>
                <v-row align="center" class="mb-5">
                  <v-col class="text-center" cols="12"
                    ><v-btn
                      class="text-center"
                      color="primary"
                      block
                      large
                      rounded
                      @click="handleSubmit"
                    >
                      <strong>提交修改</strong>
                      <v-icon>mdi-swap-horizontal</v-icon></v-btn
                    ></v-col
                  >
                </v-row>
              </v-form>
              <v-card-text>{{ text }}</v-card-text>
            </v-card>
            -->
          </v-tab-item>
          <!-- 动态 -->
          <v-tab-item :value="'notice'">
            <!-- <v-card class="mx-auto mt-5 mb-5" color="#FAFAFA" max-width="550">
              <v-card-title>
                <v-icon left> mdi-heart </v-icon>
                <span class="title font-weight-light">赞了我的文章</span>
              </v-card-title>
              <v-card-actions>
                <v-list-item class="grow">
                  <v-list-item-avatar color="grey darken-3">
                    <v-img
                      class="elevation-6"
                      src="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
                    ></v-img>
                  </v-list-item-avatar>
                  <v-list-item-content>
                    <v-list-item-title>Y3lq</v-list-item-title>
                    <v-list-item-subtitle>2022-8-1</v-list-item-subtitle>
                  </v-list-item-content>

                  <v-row align="center" justify="end">
                    <v-btn icon>
                      <v-icon>mdi-delete</v-icon>
                    </v-btn>
                  </v-row>
                </v-list-item>
              </v-card-actions>
            </v-card> -->
            <!-- <v-card class="mx-auto mt-5 mb-5" color="#FAFAFA" max-width="550">
              <v-card-title>
                <v-icon left> mdi-heart </v-icon>
                <span class="title font-weight-light">赞了我的评论</span>
              </v-card-title>



              <v-card-actions>
                <v-list-item class="grow">
                  <v-list-item-avatar color="grey darken-3">
                    <v-img
                      class="elevation-6"
                      src="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
                    ></v-img>
                  </v-list-item-avatar>
                  <v-list-item-content>
                    <v-list-item-title>Y3lq</v-list-item-title>
                    <v-list-item-subtitle>2022-8-1</v-list-item-subtitle>
                  </v-list-item-content>

                  <v-row align="center" justify="end">

                    <v-btn icon>
                      <v-icon>mdi-delete</v-icon>
                    </v-btn>

                  </v-row>
                </v-list-item>
              </v-card-actions>
            </v-card>
            <v-card class="mx-auto mt-5 mb-5" color="#FAFAFA" max-width="550">
              <v-card-title>
                <v-icon left> mdi-chat </v-icon>
                <span class="title font-weight-light">回复我的评论</span>
              </v-card-title>

              <v-card-text> 这是一条新回复 </v-card-text>

              <v-card-actions>
                <v-list-item class="grow">
                  <v-list-item-avatar color="grey darken-3">
                    <v-img
                      class="elevation-6"
                      src="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
                    ></v-img>
                  </v-list-item-avatar>

                  <v-list-item-content>
                    <v-list-item-title>Y3lq</v-list-item-title>
                    <v-list-item-subtitle>2022-8-1</v-list-item-subtitle>
                  </v-list-item-content>

                  <v-row align="center" justify="end">
         

                    <v-btn icon>
                      <v-icon>mdi-chat</v-icon>
                    </v-btn>
                    <v-btn icon>
                      <v-icon>mdi-delete</v-icon>
                    </v-btn>

                  </v-row>
                </v-list-item>
              </v-card-actions>
            </v-card>
            <v-card class="mx-auto mt-5 mb-5" color="#FAFAFA" max-width="550">
              <v-card-title>
                <v-icon left> mdi-chat </v-icon>
                <span class="title font-weight-light">回复我的评论</span>
              </v-card-title>

              <v-card-text> 这是一条新回复 </v-card-text>

              <v-card-actions>
                <v-list-item class="grow">
                  <v-list-item-avatar color="grey darken-3">
                    <v-img
                      class="elevation-6"
                      src="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
                    ></v-img>
                  </v-list-item-avatar>

                  <v-list-item-content>
                    <v-list-item-title>Y3lq</v-list-item-title>
                    <v-list-item-subtitle>2022-8-1</v-list-item-subtitle>
                  </v-list-item-content>

                  <v-row align="center" justify="end">

                    <v-btn icon>
                      <v-icon>mdi-chat</v-icon>
                    </v-btn>
                    <v-btn icon>
                      <v-icon>mdi-delete</v-icon>
                    </v-btn>

                  </v-row>
                </v-list-item>
              </v-card-actions>
            </v-card>
            <v-card class="mx-auto mt-5 mb-5" color="#FAFAFA" max-width="550">
              <v-card-title>
                <v-icon left> mdi-chat </v-icon>
                <span class="title font-weight-light">回复我的评论</span>
              </v-card-title>

              <v-card-text> 这是一条新回复 </v-card-text>

              <v-card-actions>
                <v-list-item class="grow">
                  <v-list-item-avatar color="grey darken-3">
                    <v-img
                      class="elevation-6"
                      src="https://avataaars.io/?avatarStyle=Transparent&topType=ShortHairShortCurly&accessoriesType=Prescription02&hairColor=Black&facialHairType=Blank&clotheType=Hoodie&clotheColor=White&eyeType=Default&eyebrowType=DefaultNatural&mouthType=Default&skinColor=Light"
                    ></v-img>
                  </v-list-item-avatar>
                  <v-list-item-content>
                    <v-list-item-title>Y3lq</v-list-item-title>
                    <v-list-item-subtitle>2022-8-1</v-list-item-subtitle>
                  </v-list-item-content>

                  <v-row align="center" justify="end">
               
                    <v-btn icon>
                      <v-icon>mdi-chat</v-icon>
                    </v-btn>
                    <v-btn icon>
                      <v-icon>mdi-delete</v-icon>
                    </v-btn>

                  </v-row>
                </v-list-item>
              </v-card-actions>
            </v-card>


            <v-card max-width="550" class="mx-auto mt-5 mb-5" color="#FAFAFA">
              <v-list-item>

                <v-list-item-content>
                  <v-list-item-title class="headline">新文章</v-list-item-title>
                  <v-list-item-subtitle>by Y3lq</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>

              <v-img
                src="https://cdn.vuetifyjs.com/images/cards/mountain.jpg"
                height="194"
              ></v-img>

              <v-card-text>
                Visit ten places on our planet that are undergoing the biggest
                changes today.
              </v-card-text>

              <v-card-actions>
                <v-btn text> 查看更多 </v-btn>

                <v-spacer></v-spacer>
                <v-btn icon>
                  <v-icon>mdi-heart</v-icon>
                </v-btn>
                <v-btn icon>
                  <v-icon>mdi-delete</v-icon>
                </v-btn>
              </v-card-actions>
            </v-card> -->
            <v-card max-width="550" class="mx-auto mb-5" color="#FAFAFA">
              <v-list-item>

                <v-list-item-content>
                  <v-list-item-title class="headline"
                    >系统通知</v-list-item-title
                  >
                  <v-list-item-subtitle>by Y3lq</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>

              <v-card-text
                >动态系统待开发
              </v-card-text>

              <v-card-actions>

                <v-spacer></v-spacer>

                <v-btn icon>
                  <v-icon>mdi-delete</v-icon>
                </v-btn>
              </v-card-actions>
            </v-card>
          </v-tab-item>

        </v-tabs>
      </div>
    </v-dialog>
  </v-row>
</template>

<script>
export default {
  data() {
    return {
      // userData: {
      //   username: "",
      //   nickname: "",
      //   email: "",
      //   wechat: "",
      //   website: "",
      // },
      // receiveMail: undefined,
      // receiveNew: undefined,
      // showPersonDate: undefined,
      tab: null,
      // text: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
      //   icons: false,
      //   centered: false,
      grow: true,
      //   vertical: false,
      //   prevIcon: false,
      //   nextIcon: false,
      //   right: false,
      // tabs: 2,
      // dialog: false,
    };
  },
  methods: {
    // 处理已读消除红点提示
    handleEditProfile() {
      this.$store.dispatch("snackbar/openSnackbar", {
        msg: "待开发",
        color: "info",
      });
    },
    handleReaded() {
      this.$store.state.user.newNum = 0;
      this.$store.state.user.newNumVisible = false;
    },
  },
  computed: {
    dialog: {
      get() {
        return this.$store.state.profile.dialog;
      },
      set(newValue) {
        this.$store.commit("profile/CLOSE_PROFILE", newValue);
      },
    },

    // dialog() {
    //   return this.$store.state.profile.dialog;
    // },
    avatar() {
      return this.$store.state.profile.avatar;
    },
    username() {
      return this.$store.state.profile.username;
    },
    nickname() {
      return this.$store.state.profile.nickname;
    },
    email() {
      return this.$store.state.profile.email;
    },
    wechat() {
      return this.$store.state.profile.wechat;
    },
    website() {
      return this.$store.state.profile.website;
    },
    // receiveMail: {
    //   get() {
    //     return this.$store.state.profile.receiveMail;
    //   },
    //   set(newValue) {
    //     changeUserSetting(newValue, undefined, undefined).then((res) => {
    //       this.$store.state.user.receiveMail = newValue;
    //     });
    //   },
    // },
    // receiveNew: {
    //   get() {
    //     return this.$store.state.profile.receiveNew;
    //   },
    //   set(newValue) {
    //     changeUserSetting(undefined, newValue, undefined).then((res) => {
    //       this.$store.state.user.receiveNew = newValue;
    //     });
    //   },
    // },
    // showPersonDate: {
    //   get() {
    //     return this.$store.state.profile.showPersonDate;
    //   },
    //   set(newValue) {
    //     changeUserSetting(undefined, undefined, newValue).then((res) => {
    //       this.$store.state.user.showPersonDate = newValue;
    //     });
    //   },
    // },
  },
};
</script>