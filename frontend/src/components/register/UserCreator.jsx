import RegisterForm from './RegisterForm';
import FormHat from '../ui/FormHat';
import {useSelector} from 'react-redux';
import LoadingData from '../ui/LoadingData';
import LoadingAlert from '../ui/LoadingAlert';
import {selectUserState} from '../../store/user/userSelectors';
import {cleanUserStatus} from "../../store/user/userSlice";

const UserCreator = () => {
  const {status, error} = useSelector(selectUserState);

  return (
    <>
      <LoadingData status={status} mb={3} />
      <FormHat title="Register" />
      <LoadingAlert status={status} error={error} cleanErrorAction={cleanUserStatus}/>
      <RegisterForm />
    </>
  );
};

export default UserCreator;
