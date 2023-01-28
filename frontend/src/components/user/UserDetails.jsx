import {Typography} from '@mui/material';
import {useSelector} from 'react-redux';
import {selectUser} from '../../store/user/userSelectors';
import UserPaymentList from './UserPaymentList';

const UserDetails = () => {
  const user = useSelector(selectUser);

  return (
    <>
      <Typography m={2} variant="h4" component="h1">
        {user.firstName} {user.lastName}
      </Typography>
      <UserPaymentList user={user} />
    </>
  );
};

export default UserDetails;
