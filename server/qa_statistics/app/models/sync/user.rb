class User
  include Mongoid::Document
  include Mongoid::Timestamps
  store_in collection: 'users'

  # before_save :ensure_authentication_token!

  # Include default devise modules. Others available are:
  # :token_authenticatable, :confirmable,
  # :lockable, :timeoutable and :omniauthable
  devise :database_authenticatable, :recoverable

  ## Database authenticatable
  field :email,              type: String, default: ""
  field :encrypted_password, type: String, default: ""

  # Reset password token
  field :reset_password_token, type: String
  field :reset_password_sent_at, type: Time

  field :user_type, :type => String #"normal" or "troublemaker"
  field :min_allowed_version, type: Integer


  has_many :goals, class_name: "Sync::Goal", dependent: :destroy
  has_many :logs, class_name: "Sync::Log", dependent: :destroy

end
