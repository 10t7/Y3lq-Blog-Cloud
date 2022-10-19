<template>
  <v-app>
    <core-app-bar />

    <core-drawer />

    <core-view />

    <core-footer />

    <Snackbar />

    <Profile />

    <write-dialog/>


  </v-app>
</template>

<script>
import Snackbar from "@/components/Snackbar";
import Profile from "@/components/Profile";
import WriteDialog from "@/components/WriteDialog";




export default {
  name: "App",

  metaInfo: {
    titleTemplate: "%s | Y3LQ",
  },

  components: {
    Snackbar,
    Profile,
    WriteDialog,

    CoreAppBar: () => import("@/components/core/AppBar"),
    CoreDrawer: () => import("@/components/core/Drawer"),
    CoreFooter: () => import("@/components/core/Footer"),
    CoreView: () => import("@/components/core/View")

  },
};
</script>

<style>
.v-application {
  background: #c9c9c9 !important;
}
#hero h1 {
  letter-spacing: 4px !important;
}
</style>
