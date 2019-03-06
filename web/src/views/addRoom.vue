<template>
  <div id="addRoom">
    <h1>Add your amazing property!</h1>
    <form>
      <label>Name of your property: </label>
      <input type="text" v-model="roomProperty.title" required/>
      <label>Add picture: </label>
      <input type="file" accept="image/*" id="file-input">
      <label>Location of your property: </label>
      <div id="InAadressDiv" style="width: 600px; height: 450px"></div>
      <input type="hidden" id="aadress" name="aadress">
      <label>Count of rooms: </label>
      <input type="number" v-model="roomProperty.room_count" required/>
      <label>Count of beds: </label>
      <input type="number" v-model="roomProperty.bed_count" required/>
      <label>Description your property: </label>
      <input type="text" v-model="roomProperty.description" required/>
      <label>Price per night: </label>
      <input type="number" v-model="roomProperty.price" required/>
      <button v-on:click.prevent="post">Add Property</button>
    </form>
  </div>
</template>

<script>
  import axios from 'axios';
  //import {required, minLength} from 'vuelidate/lib/validators'

  export default {

    data() {
      return {
        roomAddress: "",
        roomProperty: {
          title: null,
          address: "",
          room_count: "",
          bed_count: "",
          price: "",
          description: "",
          pic_url: "",

        },

      }
    },
    mounted() {
      this.$loadScript("http://inaadress.maaamet.ee/inaadress/js/inaadress.min.js")
        .then(() => {
          let inAadress = new InAadress({
            "container": "InAadressDiv",
            "mode": "3",
            "nocss": false,
            "lang": "en",
            "appartment": 0,
            "ihist": "1993"
          });
          document.addEventListener('addressSelected', function (e) {
            //this.roomProperty.address = e.detail[0].aadress;
            //console.log(e.detail[0].aadress);
            document.getElementById("aadress").value = e.detail[0].aadress;
            //this.roomAddress = e.detail[0].aadress;
            //console.log(this.roomAddress);

          });

        })


    },

    methods: {
      post: function () {
        this.$http.post("http://localhost:8080/api/property/add", {
          title: this.roomProperty.title,
          address: document.getElementById("aadress").value,
          room_count: this.roomProperty.room_count,
          bed_count: this.roomProperty.bed_count,
          description: this.roomProperty.description,
          owner_id: 1,
          price: this.roomProperty.price,
          pic_url: null,
        }).then(function (data) {
          console.log(data);
          //say that submitted
        })
      },
      /*uploadImage(event) {
        this.pic_url = event.target.files[0]

      },
      uploadPic() {
        let config = {
          header: {
            'Content-Type': 'image/png',

          }
        }
        const fd = new FormData();
        fd.append('image', this.pic_url, this.pic_url.name)
        axios.post('http://dijkstra.cs.ttu.ee/~egpalk/tehnika', fd, config).then(res => {
          console.log(res)
        })

      },*/

    }
  }
</script>

<style scoped>

  #addRoom {
    box-sizing: border-box;
  }

  #addRoom {
    margin: 20px auto;
    max-width: 600px;
  }

  lable {
    display: block;
    margin: 20px 0 10px;
  }

  input {
    width: 100%;
    padding: 12px 20px;
    margin: 8px 0;
    box-sizing: border-box;
    border: 2px solid gray;
    border-radius: 4px;
  }

  #file-input {
    border: none;
    box-sizing: border-box;
  }

</style>
