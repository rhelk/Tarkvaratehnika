<template>
    <div class=container style="margin-top: 20px">
        <div class="row">
            <div class="col-lg-8 col-md-12 col-xs-6">
                <b-img :src="roomDetails.pic_url" fluid alt="Responsive image"/>
                <h1 style="margin-top: 20px">{{roomDetails.title}}</h1>
                <p> <b>Location:</b> {{roomDetails.county + ", " + roomDetails.municipality + ", " + roomDetails.settlement + "," +
                    roomDetails.street }}</p>
                <p><b>Number of rooms:</b> {{roomDetails.room_count}}</p>
                <p><b>Number of beds:</b> {{roomDetails.bed_count}}</p>
                <p><b>Property description:</b> {{roomDetails.description}}</p>

            </div>
            <div id="price" class="col-lg-4 col-md-8 col-xs-12">
                <h2>Price</h2>
                <p>Price per night: {{roomDetails.price}}</p>
                <HotelDatePicker style="margin-bottom: 30px" format="DD/MM/YYYY"
                                 @check-in-changed="setCheckinDate"
                                 @check-out-changed="setCheckOutDate"
                                 :disabledDates="disabledDates"
                                 :key="disabledDates.length"
                >
                </HotelDatePicker>
                <b-button v-if="userCanRent" v-bind:disabled="hasClicked" variant="dark" class="my-2 my-sm-0"
                          type="submit"
                          v-on:click.prevent="rentProperty">Request rental
                </b-button>
            </div>
        </div>
    </div>

</template>

<script>

    import HotelDatePicker from 'vue-hotel-datepicker'

    export default {
        components: {
            HotelDatePicker,
        },
        props: {
            startingDateValue: {
                default: null,
                type: Date
            },
            endingDateValue: {
                default: null,
                type: Date
            },
        },

        data: function () {
            return {
                date: '',
                disabledDates: [], //['2019-05-05', '2019-05-07']
                checkIn: "",
                checkOut: "",
                userCanRent: true,
                hasClicked: false,
                roomDetails: [],
                id: this.$route.params.id,
            }
        },
        created() {
            const that = this;
            //if (this.$store.getters.isLoggedIn) {
            //DISABLE DATES
            this.$store.dispatch('doGet', {url: 'rent/dates/' + this.id}).then(data => {
                for (let i = 0; i < data.data.length; i++) {
                    let start = new Date(data.data[i].start);
                    let end = new Date(data.data[i].end);
                    for (start; start <= end; start.setDate(start.getDate() + 1)) {
                        let month = (parseInt(start.getMonth()) + 1).toString();
                        let day = start.getDate().toString();
                        if (month.length < 2) month = '0' + month;
                        if (day.length < 2) day = '0' + day;
                        this.disabledDates.push(start.getFullYear() + "-" + month + "-" + day);
                    }
                }
            });
            //}

            //GET PROPERTY DATA
            this.$store.dispatch('doGet', {url: 'property/get/' + this.id}).then(data => {
                that.roomDetails = data.data;
                if (data.data.users.user_id === this.$store.getters.getUser_id) {
                    that.userCanRent = false;
                }
            });

        },

        methods: {
            rentProperty: function () {
                this.hasClicked = true;
                if (!this.$store.getters.isLoggedIn) {
                    this.$router.push({path: `/login`});
                    this.$router.go();
                } else {
                    if(this.checkIn !== "" || this.checkOut !== "" || this.checkIn===this.checkOut){
                    this.$store.dispatch('doPost', {
                        url: 'rent/to_rent/' + this.id, body:
                            {
                                start: this.checkIn,
                                end: this.checkOut
                            }

                    }).then(data => {
                        this.$router.push({path: `/all`});

                    })}
                    else{
                        alert("Please enter valid rental period");
                        this.hasClicked = false;
                    }
                }


            },
            setCheckinDate(date) {
                if (date != null) {
                    let month = (parseInt(date.getMonth()) + 1).toString();
                    let day = date.getDate().toString();
                    if (month.length < 2) month = '0' + month;
                    if (day.length < 2) day = '0' + day;
                    this.checkIn = date.getFullYear() + "-" + month + "-" + day;

                }
            },
            setCheckOutDate(date) {
                if (date != null) {
                    let month = (parseInt(date.getMonth()) + 1).toString();
                    let day = date.getDate().toString();
                    if (month.length < 2) month = '0' + month;
                    if (day.length < 2) day = '0' + day;
                    this.checkOut = date.getFullYear() + "-" + month + "-" + day;

                }
            },
        }
    }
</script>

<style scoped>


    #price {
        background: whitesmoke;
        height: 250px;

    }

    h2 {
        font-family: Arial;
    }

</style>
