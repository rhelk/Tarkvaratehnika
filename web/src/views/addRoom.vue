<template>
  <div id="addRoom">
    <h1>Add your amazing property!</h1>
    <form>
      <label>Name of your property: </label>
      <input type="text" v-model="roomProperty.title" required/>
      <label>Add picture: </label>
      <input type="file" class="file-select" @change="handleFileUploadChange" accept="image/*" id="file-input">
      <label>Location of your property: </label>
      <div id="InAadressDiv" style="width: 600px; height: 450px"></div>
      <input type="hidden" id="address" name="address">
      <label>Count of rooms: </label>
      <input type="number" v-model="roomProperty.room_count" required/>
      <label>Count of beds: </label>
      <input type="number" v-model="roomProperty.bed_count" required/>
      <label>Description your property: </label>
      <input type="text" v-model="roomProperty.description" required/>
      <label>Price per night: </label>
      <input type="number" v-model="roomProperty.price" required/>
      <button v-on:click.prevent="handler" class="file-submit">Add Property</button>
    </form>
  </div>
</template>

<script>
  import axios from 'axios';
  import {DataSnapshot as storageRef} from "firebase";
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
          pic_name: "",

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

          document.addEventListener('addressSelected', function(e){
            inAadress.hideResult();
          });

          document.addEventListener('addressSelected', function(e){
            let aadress = e.detail[0].aadress;
            aadress = aadress.split(", ").reverse().join(", ");
            inAadress.setAddress(aadress);
          });

          document.addEventListener('addressSelected', (e) => {
            const self = this;
            //document.getElementById("address").value = e.detail[0].aadress;
            self.roomProperty.address = e.detail[0].aadress;
            console.log(e.detail[0].aadress);
            console.log(e.detail[0].maakond);
            console.log(e.detail[0].omavalitsus);
            console.log(e.detail[0].asustusyksus);
            console.log(e.detail[0].liikluspind);
            console.log(e.detail[0].aadress_nr);

          });

        })

    },

    methods: {
      handler: function() { //Syntax assuming its in the 'methods' option of Vue instance
        this.handleFileUploadSubmit();
        //this.post();
      },
      post: function () {
        this.$http.post("http://localhost:8080/api/property/add", {
          title: this.roomProperty.title,
          //address: document.getElementById("address").value,
          address: this.roomProperty.address,
          room_count: this.roomProperty.room_count,
          bed_count: this.roomProperty.bed_count,
          description: this.roomProperty.description,
          owner_id: 1,
          price: this.roomProperty.price,
          pic_url: this.roomProperty.pic_url,
        }).then(function (data) {
          console.log(data);
          //say that submitted
        })
      },

      handleFileUploadChange(e) {
        this.roomProperty.pic_name = e.target.files[0];
        console.log(this.roomProperty.pic_name)
      },
      handleFileUploadSubmit(e) {
        const storageService = firebase.storage();
        const storageRef = storageService.ref();
        const uploadTask = storageRef.child(`images/${this.roomProperty.pic_name.name}`).put(this.roomProperty.pic_name); //create a child directory called images, and place the file inside this directory
       // this.roomProperty.pic_url =  storageRef.child('images/' + this.roomProperty.pic_name.name).getDownloadURL().toString();
        uploadTask.on('state_changed', (snapshot) => {
          // Observe state change events such as progress, pause, and resume
        }, (error) => {
          // Handle unsuccessful uploads
          console.log(error);
        }, () => {
          // Do something once upload is complete
          console.log('success');
          /*const getPicUrl = storageRef.child('images/' + this.roomProperty.pic_name.name).getDownloadURL();
          console.log(getPicUrl);
          //this.roomProperty.pic_url = getPicUrl;
            this.post();*/
          uploadTask.snapshot.ref.getDownloadURL().then((downloadURL) => {
            //after uploading picture, get picture url and then post it to database
            const self = this;
            console.log('File available at', downloadURL);
            self.roomProperty.pic_url = downloadURL;
            this.post();
          });
        });


      }
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
