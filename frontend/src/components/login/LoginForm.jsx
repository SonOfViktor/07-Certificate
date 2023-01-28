import {Stack, TextField} from '@mui/material';
import {useForm} from 'react-hook-form';
import {useDispatch} from 'react-redux';
import {cleanUserStatus, loadUser} from '../../store/user/userSlice';
import ConfirmResetButton from '../ui/ConfirmResetButton';
import LoadingAlert from '../ui/LoadingAlert';
import {loginValidation} from './validation/validation';

const LoginFormWrapper = ({children, ...props}) => (
  <Stack
    component="form"
    {...props}
    gap="10px"
    sx={{
      '& input': {
        fontSize: 16,
        textAlign: 'center',
      },
      '& input:placeholder-shown': {
        textAlign: 'center',
      },
    }}>
    {children}
  </Stack>
);

const LoginForm = ({status, error}) => {
  const dispatch = useDispatch();

  const {
    handleSubmit,
    register,
    reset,
    formState: {errors, isValid},
  } = useForm({
    mode: 'onTouched',
    defaultValues: {
      email: '',
      password: '',
    },
  });

  const onSubmit = data => {
    dispatch(loadUser(data));
    reset();
  };

  return (
    <LoginFormWrapper noValidate onSubmit={handleSubmit(onSubmit)}>
      <LoadingAlert status={status} error={error} cleanErrorAction={cleanUserStatus}/>

      <TextField
        type="email"
        size="small"
        placeholder="Login"
        autoFocus
        {...register('email', loginValidation)}
        error={!!errors.email?.message}
        helperText={errors.email?.message}
      />
      <TextField
        type="password"
        name="password"
        size="small"
        placeholder="Password"
        {...register('password', {required: "The field can't be empty"})}
        error={!!errors.password?.message}
        helperText={errors.password?.message}
      />
      <ConfirmResetButton type="submit" disabled={!isValid}>
        Login
      </ConfirmResetButton>
    </LoginFormWrapper>
  );
};

export default LoginForm;
