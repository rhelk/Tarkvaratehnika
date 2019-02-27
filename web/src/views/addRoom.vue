<template>
  <div id="addRoom">
    <h1>Add your amazing property!</h1>
    <form>
      <label>Name of your property: </label>
      <input type="text" v-model="roomProperty.title" required/>
      <label>Add picture: </label>
      <input type="file" accept="image/*" @change="uploadImage($event)" id="file-input">
      <label>Location of your property: </label>
      <input type="text" v-model="roomProperty.address" required/>
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

  export default {
    data() {
      return {
        roomProperty: {
          title: "",
          address: "",
          room_count:"",
          bed_count:"",
          price:"",
          description:"",
          pic_url:"",

        }
      }
    },
    methods:{
      post:function () {
        this.$http.post("", {
          title:this.roomProperty.title,
          address: this.roomProperty.address,
          room_count: this.roomProperty.room_count,
          description: this.roomProperty.description,
          owner_id: 1,
          price: this.roomProperty.price
        }).then(function(data){
          console.log(data);
          //say that submitted
        })
      },
      uploadImage(event) {

        const URL = '';/dijkstra url/

        let data = new FormData();
        data.append('name', 'my-picture');
        data.append('file', event.target.files[0]);

        let config = {
          header : {
            'Content-Type' : 'image/png'
          }
        }

        axios.put(
          URL,
          data,
          config
        ).then(
          response => {
            console.log('image upload response > ', response)
          }
        )
      }
    }
  }
</script>

<style scoped>

  #addRoom{
    box-sizing: border-box;
  }
  #addRoom{
    margin: 20px auto;
    max-width: 600px;
  }

  lable{
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
   #file-input{
    border: none;
     box-sizing: border-box;
  }

</style>
