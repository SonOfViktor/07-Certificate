const EMAIL_PATTERN = /^([^<>()[\]\\.,;:\s@"]*)@(([a-zA-Z\-\d]+\.)+[a-zA-Z]{2,})$/;
const NAME_PATTERN = /^[a-zA-Z]{2,20}$|^[А-ЯЁа-яё]{2,20}$/;
const PASSWORD_PATTERN = /^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])[a-zA-Z\d]{4,30}$/;

const generalRules = {required: "The field can't be empty"};

export const registerFields = password => [
  {
    id: 'email',
    type: 'email',
    label: 'Login Name',
    rules: {
      ...generalRules,
      maxLength: {
        value: 30,
        message: "Email can't be more than 30 symbols",
      },
      pattern: {
        value: EMAIL_PATTERN,
        message: 'Invalid email address',
      },
    },
  },
  {
    id: 'firstName',
    type: 'text',
    label: 'First Name',
    rules: {
      ...generalRules,
      pattern: {
        value: NAME_PATTERN,
        message: 'First name must contain 2 - 20 Latin or Cyrillic letters',
      },
    },
  },
  {
    id: 'lastName',
    type: 'text',
    label: 'Last Name',
    rules: {
      ...generalRules,
      pattern: {
        value: NAME_PATTERN,
        message: 'Last name must contain 2 - 20 Latin or Cyrillic letters',
      },
    },
  },
  {
    id: 'password',
    type: 'password',
    label: 'Password',
    rules: {
      ...generalRules,
      pattern: {
        value: PASSWORD_PATTERN,
        message:
          'Password must contain 4 - 30 symbols and includes digits, upper and lower case letters',
      },
    },
  },
  {
    id: 'repeatPassword',
    type: 'password',
    label: 'Repeat Password',
    rules: {
      ...generalRules,
      pattern: {
        value: PASSWORD_PATTERN,
        message:
          'Repeat password must contain 4 - 30 symbols and includes digits, upper and lower case letters',
      },
      validate: value => {
        if (password !== value) {
          return 'Password and repeat password must match';
        }
      },
    },
  },
];
