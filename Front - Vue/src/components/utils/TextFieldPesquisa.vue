<template>
    <v-text-field class="mt-5" label="Pesquise alguma pergunta..." color="verdeClaro"
        placeholder="Ex: 'Um tratamento com corticoide pode alterar minha glicose?'" outlined v-model="pesquisa" rounded
        append-icon="mdi-magnify" @click:append="clickPesquisar" v-on:keyup.enter="clickEnter">
    </v-text-field>
</template>

<script>
export default {
    name: 'TextFieldPesquisa',
    props:{
        texto: {
            type: String,
            default: ''
        }
    },
    data(){
        return{
            pesquisa: this.texto,
        }
    },
    methods:{
        clickPesquisar(){
            this.$emit('clickPesquisar')
        },
        clickEnter(){
            this.$emit('clickEnter')
        }
    },
    watch:{
        pesquisa(novoValor){
            this.$emit('input', novoValor)
        }
    }
}
</script>

<style>

</style>