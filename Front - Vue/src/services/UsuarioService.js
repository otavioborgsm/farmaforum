import VueJwtDecode from 'vue-jwt-decode'
import store from '@/store'

export function login(usuario){
    localStorage.setItem('token', usuario.token)
    localStorage.setItem('idUsuario', usuario.id)

    if (usuario.isAdmin){
        localStorage.setItem('isAdmin', usuario.isAdmin)
    }

    if (usuario.farmaceutico){
        localStorage.setItem('farmaceutico', usuario.farmaceutico)
    }
}

export function getUsuario() {
    const token = localStorage.getItem('token')

    if (token === null || !validarToken(token)){
        store.dispatch("usuario", null)
        return null
    }
    
    const usuario = new Object({
        id: localStorage.getItem('idUsuario'),
        token: token,
        farmaceutico: null,
        isAdmin: false
    })

    const idFarmaceutico = localStorage.getItem('farmaceutico')
    const isAdmin = localStorage.getItem('isAdmin') === "true"

    if(idFarmaceutico){
        usuario.farmaceutico = idFarmaceutico
    }
    if(isAdmin){
        usuario.isAdmin = true
    }

    return usuario
}

export function getToken(){
    const token = localStorage.getItem('token')
    if(validarToken(token)){
        return token
    } else{
        return null
    }
}

export function deleteToken(){
    localStorage.removeItem('token')
}

export function logout(){
    store.dispatch("usuario", null)
    localStorage.clear()
}

function validarToken(token){
    if(token){
        const decodeToken = VueJwtDecode.decode(token)
        const dataAgora = Date.now()
        if(dataAgora > (decodeToken.exp * 1000)){
            return false
        }else{
            return true
        }
    }else{
        return false
    }
    
}