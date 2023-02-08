import {useEffect} from 'react';
import {useDispatch, useSelector} from 'react-redux';
import {useNavigate} from 'react-router-dom';
import {cleanUserStatus, createUser} from '../../../store/user/userSlice';
import {selectUserStatus} from '../../../store/user/userSelectors';

export const useRegisterForm = reset => {
  const dispatch = useDispatch();
  const navigate = useNavigate();
  const status = useSelector(selectUserStatus);

  useEffect(() => {
    if (status === 'created') {
      reset();
      navigate('/login');
    }
  }, [status, reset, navigate]);

  const onSubmit = data => {
    dispatch(createUser(data));
  };

  const handleReset = () => {
    reset();
    dispatch(cleanUserStatus());
  };

  return [onSubmit, handleReset];
};
