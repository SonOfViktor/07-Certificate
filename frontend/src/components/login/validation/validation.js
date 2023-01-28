const EMAIL_PATTERN = /^([^<>()[\]\\.,;:\s@"]*)@(([a-zA-Z\-\d]+\.)+[a-zA-Z]{2,})$/;

export const loginValidation = {
  required: "The field can't be empty",
  pattern: {
    value: EMAIL_PATTERN,
    message: 'Invalid email address',
  },
};
