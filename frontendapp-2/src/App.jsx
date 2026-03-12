import './App.css'
import { Routes, Route } from 'react-router-dom'
import { SignUp } from './components/SignUp'
import 'bootstrap/dist/css/bootstrap.min.css'
import { SignIn } from './components/SignIn'
import { ShowAllData } from './components/ShowAllData'
function App() {
  
  return (<>
     
      <Routes>
         <Route path='/signup' element={<SignUp />} />

         <Route path='/' element={<SignIn />}/>

         <Route exact path='/customers' element={<ShowAllData />}  />
      </Routes>
    
    </>
  )
}

export default App
