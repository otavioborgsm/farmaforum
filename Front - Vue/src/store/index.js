import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    usuario: null
  },
  getters: {
    usuario: (state) =>{
      return state.usuario;
    }
  },
  mutations: {
    usuario(state, usuario){
      state.usuario = usuario;
    }
  },
  actions: {
    usuario(context, usuario){
      context.commit('usuario', usuario)
    }
  },
  modules: {
  }
})
