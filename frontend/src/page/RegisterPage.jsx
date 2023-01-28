import {Container} from '@mui/material'
import UserCreator from '../components/register/UserCreator';

const RegisterPage = () => {
  return (
    <main className="main-flex">
      <Container maxWidth="md" sx={{padding: 0}}>
        <UserCreator />
      </Container>
    </main>
  );
};

export default RegisterPage;
