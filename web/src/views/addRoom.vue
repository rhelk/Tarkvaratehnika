<template>
  <div id="addRoom">
    <h1>Add your amazing property!</h1>

    <b-form v-if="!submitted">
      <b-form-input required data-vv-name="title" type="text" v-model="roomProperty.title"
                    placeholder="Name of your property"
                    v-validate="{ required: true, min:4 }"
                    :state="validateState('roomProperty.title')"/>
      <b-form-text><span style="color: red">{{ errors.first('title') }}</span></b-form-text>

      <label>Location of your property: </label>
      <div id="InAadressDiv" style="width: 600px; height: 450px"></div>

      <b-form-input data-vv-name="rooms" type="number" v-model="roomProperty.room_count" placeholder="Count of rooms"
                    required
                    v-validate="{ required: true, min_value:1}"
                    :state="validateState('roomProperty.room_count')"/>
      <b-form-text><span style="color: red">{{ errors.first('rooms') }}</span></b-form-text>

      <b-form-input data-vv-name="beds" type="number" v-model="roomProperty.bed_count" placeholder="Count of beds"
                    required
                    v-validate="{ required: true, min_value:1}"
                    :state="validateState('roomProperty.bed_count')"/>
      <b-form-text><span style="color: red">{{ errors.first('beds') }}</span></b-form-text>

      <b-form-input type="text" v-model="roomProperty.description" placeholder="Description your property"/>
      <b-form-input data-vv-name="price" type="number" v-model="roomProperty.price" placeholder="Price for one nigh"
                    required
                    v-validate="{ required: true, min_value:5}"
                    :state="validateState('roomProperty.price')"/>
      <b-form-text><span style="color: red">{{ errors.first('price') }}</span></b-form-text>

      <b-form-file required data-vv-name="pic" v-model="file" type="file" class="file-select"
                   @change="handleFileUploadChange"
                   accept="image/*" id="file-input"
                   v-validate="{ required: true}"
                   :state="validateState(file)"
                   placeholder="Choose a picture..."/>
      <b-form-text><span style="color: red">{{ errors.first('pic') }}</span></b-form-text>
      <p></p>
      <b-button type="submit" variant="primary" :disabled="errors.any() || !isComplete" class="btn btn-primary" v-on:click.prevent="handler">Add Property
      </b-button>
    </b-form>

  </div>
</template>

<script>


  export default {

    data() {
      return {
        file: null,
        submitted: false,
        roomProperty: {
          title: "",
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
    computed: {
      isComplete () {
        return this.roomProperty.address;
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
            "ihist": "1993",
            "searchLayers": ["TANAV"]
          });


          document.addEventListener('addressSelected', function (e) {
            let aadress = e.detail.aadress;
            aadress = aadress.split(", ").reverse().join(", ");
            inAadress.setAddress(aadress);
            inAadress.hideResult();
            console.log(aadress)
          });

          document.addEventListener('addressSelected', (e) => {
            const self = this;
            self.roomProperty.address = e.detail.aadress;
            console.log(e.detail.aadress);
            console.log(e.detail.maakond);
            console.log(e.detail.omavalitsus);
            console.log(e.detail.asustusyksus);
            console.log(e.detail.liikluspind);
            //console.log(e.detail[0].aadress_nr);

          });

        });
      this.$loadScript("https://www.gstatic.com/firebasejs/5.8.6/firebase.js")
        .then(() => {
          let config = {
            apiKey: "AIzaSyCvQ34crBffoj4X3cHf82IH86l8aBTyM1s",
            authDomain: "tarkvaratehnika-1551709647803.firebaseapp.com",
            databaseURL: "https://tarkvaratehnika-1551709647803.firebaseio.com",
            projectId: "tarkvaratehnika-1551709647803",
            storageBucket: "tarkvaratehnika-1551709647803.appspot.com",
            messagingSenderId: "805114248870"
          };
          firebase.initializeApp(config);
        });

    },
    methods: {
      handler: function () { //Syntax assuming its in the 'methods' option of Vue instance
        this.handleFileUploadSubmit();
        //this.post();
      },

      post: function () {
        this.$http.post("http://localhost:8080/api/property/add", {
          title: this.roomProperty.title,
          address: this.roomProperty.address,
          room_count: this.roomProperty.room_count,
          bed_count: this.roomProperty.bed_count,
          description: this.roomProperty.description,
          owner_id: 1,
          price: this.roomProperty.price,
          pic_url: this.roomProperty.pic_url,
        }).then(function (data) {
          console.log(data);
          this.submitted = true;
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
        uploadTask.on('state_changed', (snapshot) => {
          // Observe state change events such as progress, pause, and resume
        }, (error) => {
          // Handle unsuccessful uploads
          console.log(error);
        }, () => {
          // Do something once upload is complete
          console.log('success');
          uploadTask.snapshot.ref.getDownloadURL().then((downloadURL) => {
            //after uploading picture, get picture url and then post it to database
            const self = this;
            console.log('File available at', downloadURL);
            self.roomProperty.pic_url = downloadURL;
            this.post();
          });
        });

      },
      validateState(ref) {

        if (this.veeFields[ref] && (this.veeFields[ref].dirty || this.veeFields[ref].validated)) {
          return !this.errors.has(ref)
        }
        return null;
      },

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
