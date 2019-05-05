<template>
  <div class="container">
    <div v-if="!submitted">
      <h1>Add your amazing property!</h1>
      <div style="margin-bottom: 50px"></div>
      <b-form>
        <small><label><b>Name of your property</b></label></small>
        <b-form-input required data-vv-name="title" type="text" v-model="roomProperty.title"
                      placeholder="Name of your property"
                      v-validate="{ required: true, min:4, max: 25 }"
                      :state="validateState('roomProperty.title')"/>

        <b-form-text><span style="color: red">{{ errors.first('title') }}</span></b-form-text>

        <small><label><b>Location of your property</b></label></small>
        <div id="InAadressDiv" style="width: 600px; height: 450px"></div>


        <p></p>

        <small><label><b>Count of rooms</b></label></small>
        <b-form-input data-vv-name="rooms" type="number" v-model="roomProperty.room_count" placeholder="Count of rooms"
                      required
                      v-validate="{ required: true, min_value:1}"
                      :state="validateState('roomProperty.room_count')"/>
        <b-form-text><span style="color: red">{{ errors.first('rooms') }}</span></b-form-text>

        <small><label><b>Count of beds</b></label></small>
        <b-form-input data-vv-name="beds" type="number" v-model="roomProperty.bed_count" placeholder="Count of beds"
                      required
                      v-validate="{ required: true, min_value:1}"
                      :state="validateState('roomProperty.bed_count')"/>
        <b-form-text><span style="color: red">{{ errors.first('beds') }}</span></b-form-text>


        <small><label><b>Price for one night</b></label></small>
        <b-form-input data-vv-name="price" type="number" v-model="roomProperty.price" placeholder="Price for one nigh"
                      required
                      v-validate="{ required: true, min_value:5}"
                      :state="validateState('roomProperty.price')"/>
        <b-form-text><span style="color: red">{{ errors.first('price') }}</span></b-form-text>

        <small><label><b>Choose a picture for your property</b></label></small>
        <b-form-file required data-vv-name="pic" v-model="file" type="file" class="file-select"
                     @change="handleFileUploadChange"
                     accept="image/*" id="file-input"
                     v-validate="{ required: true}"
                     ref="myFileInput"
                     :state="validateState('file')"
                     placeholder="Choose a picture..."/>
        <b-form-text><span style="color: red">{{ errors.first('pic') }}</span></b-form-text>

        <small><label><b>Description of your property</b></label></small>
        <b-form-textarea type="text" v-model="roomProperty.description" placeholder="Description your property"/>
        <p></p>
        <b-button type="submit" variant="dark" :disabled="errors.any() || !isComplete" class="btn btn-primary"
                  v-on:click.prevent="handler" v-bind:disabled="hasClicked">Add Property
        </b-button>
      </b-form>

    </div>
    <div v-else>
      <h1>You have successfully added a property</h1>
    </div>
  </div>
</template>

<script>


  export default {

    data() {
      return {
        hasClicked: false, //disable button to prevent double click
        file: null,
        submitted: false,
        roomProperty: {
          title: "",
          address: "",
          county: "",
          municipality: "",
          settlement: "",
          street: "",
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
      isComplete() {
        return this.roomProperty.address;
      }
    },
    beforeCreate() {
      if (!this.$store.getters.isLoggedIn) {
        this.$router.push({path: `/login`});
        this.$router.go();

      }

    },
    mounted() {
      if (this.$store.getters.isLoggedIn) {
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
            });

            document.addEventListener('addressSelected', (e) => {
              const self = this;
              self.roomProperty.address = e.detail.aadress;
              self.roomProperty.county = e.detail.maakond;
              self.roomProperty.municipality = e.detail.omavalitsus;
              self.roomProperty.settlement = e.detail.asustusyksus;
              self.roomProperty.street = e.detail.liikluspind;
            });
          });
      }
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
          if (!firebase.apps.length) {
            firebase.initializeApp(config);
          }
        });

    },
    methods: {

      handler: function () {
        this.$validator.validateAll().then((result) => {
          if (result) {
            this.hasClicked = true;
            this.handleFileUploadSubmit();
          }
        });
      },


      post: function () {

        const that = this;
        this.$store.dispatch('doPost', {
          url: 'secure/property/add', body:
            {
              title: this.roomProperty.title,
              address: this.roomProperty.address,
              county: this.roomProperty.county,
              municipality: this.roomProperty.municipality,
              settlement: this.roomProperty.settlement,
              street: this.roomProperty.street,
              room_count: this.roomProperty.room_count,
              bed_count: this.roomProperty.bed_count,
              description: this.roomProperty.description,
              price: this.roomProperty.price,
              pic_url: this.roomProperty.pic_url,
            }
        }).then(data => {
          that.submitted = true;
        })
      },


      handleFileUploadChange(e) {

        if (e.target.files[0].size > 307200) {
          alert("File is too big!");
          this.file = null;
          this.roomProperty.pic_name = "";
          document.getElementById("file-input").value = "";
        } else {
          this.roomProperty.pic_name = e.target.files[0];
        }

      },
      handleFileUploadSubmit(e) {
        let d = new Date();
        let h = d.getHours().toString();
        let m = d.getMinutes().toString();
        let s = d.getSeconds().toString();
        let ms = d.getMilliseconds().toString();
        let day = d.getDay().toString();
        let nr = s + day + ms + h + m;

        const storageService = firebase.storage();
        const storageRef = storageService.ref();
        const uploadTask = storageRef.child(`images/${nr + this.roomProperty.pic_name.name}`).put(this.roomProperty.pic_name); //create a child directory called images, and place the file inside this directory
        uploadTask.on('state_changed', (snapshot) => {
          // Observe state change events such as progress, pause, and resume
        }, (error) => {
          // Handle unsuccessful uploads
          console.log(error);
        }, () => {
          // Do something once upload is complete
          uploadTask.snapshot.ref.getDownloadURL().then((downloadURL) => {
            //after uploading picture, get picture url and then post it to database
            const self = this;
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

  @media (min-width: 1200px) {
    .container {
      max-width: 630px;
    }
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
    border-radius: 4px;
  }


</style>
