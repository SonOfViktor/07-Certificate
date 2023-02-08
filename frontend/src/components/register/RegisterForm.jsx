import LabeledField from '../ui/LabeledField';
import FormButtonGroup from '../ui/FormButtonGroup';
import {registerFields} from './const/registerFields';
import GridForm from '../ui/GridForm';
import {useForm} from 'react-hook-form';
import {useRegisterForm} from './hooks/useRegisterForm';

const RegisterForm = () => {
  const {
    handleSubmit,
    control,
    watch,
    reset,
    formState: {errors, isValid},
  } = useForm({
    mode: 'onTouched',
    defaultValues: {
      email: '',
      firstName: '',
      lastName: '',
      password: '',
      repeatPassword: '',
    },
  });

  const [onSubmit, handleReset] = useRegisterForm(reset);

  return (
    <GridForm gap="10px 20px" noValidate onSubmit={handleSubmit(onSubmit)}>
      {registerFields(watch('password')).map(({id, type, label, rules}) => (
        <LabeledField
          key={id}
          id={id}
          type={type}
          label={label}
          rules={rules}
          control={control}
          error={!!errors[id]?.message}
          helperText={errors[id]?.message}
          autoFocus={id === 'email'}
        />
      ))}
      <div className="empty-cell" />
      <FormButtonGroup
        button1="Reset"
        onClick1={handleReset}
        button2="Sign up"
        disabled={!isValid}
        className="buttonPlace"
      />
    </GridForm>
  );
};

export default RegisterForm;
