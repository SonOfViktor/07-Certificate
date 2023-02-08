import React from 'react';
import {useSelector, useDispatch} from 'react-redux';
import {Navigate, useLocation} from 'react-router-dom';
import {selectUser} from '../../store/user/userSelectors';
import {expireToken} from '../../store/user/userSlice';

const SecurityAuth = ({role, children}) => {
  const dispatch = useDispatch()
  const location = useLocation();
  const user = useSelector(selectUser);

  if (!user || user.expireTime < Date.now() / 1000) {
    if (user) {
      dispatch(expireToken());
    }
    return <Navigate to="/login" state={{from: location}}/>;
  } else if (role === 'admin' && role !== user.role) {
    return <Navigate to="/not-found"/>;
  } else {
    return children;
  }
};
export default SecurityAuth;
