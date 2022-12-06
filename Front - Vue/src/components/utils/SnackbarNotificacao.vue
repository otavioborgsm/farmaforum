<template>
  <v-snackbar
      v-model="visivel"
      :timeout="timeout"
      :color="cor"
      dark
    >
        {{ texto }}
        <template v-slot:action="{ attrs }">
        <v-btn
            dark
            text
            v-bind="attrs"
            @click="clickFechar"
        >
            OK
        </v-btn>
        </template>
    </v-snackbar>
</template>

<script>
export default {
    name: "SnackbarNotificacao",
    props:{
        texto:{
            type: String,
            required: true
        },
        corSucesso:{
            type: Boolean,
            default: true
        },
        tempo:{
            type: Number,
            default: 5000
        }
    },
    data(){
        return{
            visivel : true,
            cor: this.corSucesso ? "verdeClaro" : "deep-orange darken-1",
            timeout: this.tempo
        }
    },
    methods:{
        clickFechar(){
            this.visivel = false
            this.$emit('fechaSnackbar')
        }
    },
    watch:{
        visivel(){
            setTimeout(()=>{
                this.clickFechar()
            }, 1000)
        }
    }
}
</script>

<style>

</style>