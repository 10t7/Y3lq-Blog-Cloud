<template>
  <div>
    <v-snackbar
      v-model="snackbar"
      :top="true"
      :timeout="messageTime"
      shaped
      :color="color"
    >
      {{ text }}

      <template v-slot:action="{ attrs }">
        <v-btn
          :color="buttomColor"
          text
          v-bind="attrs"
          @click="snackbar = false"
        >
          Close
        </v-btn>
      </template>
    </v-snackbar>
  </div>
</template>

<script>
export default {
  name: "Message",

  data() {
    return {
      buttomColor: "primary",
      color: undefined,
      messageTime: 3000,
      snackbar: false,
      text: "",
    };
  },
  methods: {
    openMessage(type, msg, time) {
      if (type == "success") {
        (this.messageTime = time),
          (this.text = msg),
          (this.snackbar = true);
          (this.color = ""),
          this.buttomColor = "primary"
      }
      if(type == "error"){
          (this.messageTime = time),
            
          (this.text = msg),
          (this.color = "#D84315"),
          (this.snackbar = true);
          this.buttomColor = "#000000"
      }
    },
  },

  //   computed:{

  //   },

  //   props: {
  //     time: {
  //       type: Number,
  //       require: true,
  //     },
  //     msg: {
  //       type: String,
  //       require: true,
  //     },
  //     type: {
  //       type: String,
  //     },
  //   },

  mounted() {},
};
</script>


